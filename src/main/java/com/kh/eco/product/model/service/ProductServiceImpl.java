package com.kh.eco.product.model.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.common.model.vo.PageInfo;
import com.kh.eco.product.model.dao.ProductDao;
import com.kh.eco.product.model.vo.Address;
import com.kh.eco.product.model.vo.ApproveRequest;
import com.kh.eco.product.model.vo.Brand;
import com.kh.eco.product.model.vo.Cart;
import com.kh.eco.product.model.vo.KakaoPay;
import com.kh.eco.product.model.vo.Order;
import com.kh.eco.product.model.vo.OrderItem;
import com.kh.eco.product.model.vo.Product;
import com.kh.eco.product.model.vo.ProductLike;
import com.kh.eco.product.model.vo.ProductOption;
import com.kh.eco.product.model.vo.ProductReview;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductDao dao;
	private final SqlSessionTemplate sqlSession;
	
	@Override
	public ArrayList<Product> selectProductList(Map<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return dao.selectProductList(sqlSession, map, rowBounds);
	}
	
	@Override
	public ArrayList<Product> searchProduct(String keyword) {
		return dao.searchProduct(sqlSession, keyword);
	}

	@Override
	public int addLike(ProductLike like) {
		return dao.addLike(sqlSession, like);
	}

	@Override
	public Product selectProduct(int productNo) {
		return dao.selectProduct(sqlSession, productNo);
	}

	@Override
	public String getPrice(int optionNo) {
		return dao.getPrice(sqlSession, optionNo);
	}

	@Override
	public ArrayList<String> getImages(int productNo) {
		return dao.getImages(sqlSession, productNo);
	}

	@Override
	public Brand getBrand(int productNo) {
		return dao.getBrand(sqlSession, productNo);
	}

	@Override
	public ProductReview getRate(int productNo) {
		return dao.getRate(sqlSession, productNo);
	}

	@Override
	public ArrayList<ProductReview> reviewList(int productNo) {
		return dao.reviewList(sqlSession, productNo);
	}
	public ArrayList<ProductLike> getLikes(int userNo) {
		return dao.getLikes(sqlSession, userNo);
	}

	@Override
	public String checkLike(ProductLike like) {
		return dao.checkLike(sqlSession, like)!=null? "Y":"N";
	}

	@Override
	public int removeLike(ProductLike like) {
		return dao.removeLike(sqlSession, like);
	}

	@Override
	public ArrayList<Cart> selectCartItems(int userNo) {
		return dao.selectCartItems(sqlSession, userNo);
	}

	@Override
	public int updateQty(Cart cart) {
		return dao.updateQty(sqlSession, cart);
	}

	@Override
	public int addCart(Cart cart) {
		return dao.addCart(sqlSession, cart);
	}

	@Override
	public String checkCart(Cart cart) {
		return dao.checkCart(sqlSession, cart);
	}

	@Override
	public int removeItem(Cart cart) {
		return dao.removeItem(sqlSession, cart);
	}

	@Override
	public Cart getCartItem(int optionNo) {
		return dao.getCartItem(sqlSession, optionNo);
	}
	
	@Transactional
	@Override
	public int orderProduct(Order order) {
		int orderResult = dao.insertOrder(sqlSession, order);
		int itemResult =1;
		int cartResult = 1;
		System.out.println("주문 넣기 : " +orderResult);
		if(orderResult>0) {
			//itemResult = dao.insertOrderItems(sqlSession, order);
			for(int i=0; i< order.getItems().size(); i++) {
				itemResult *= dao.insertOrderItem(sqlSession, order.getItems().get(i));
			}
			System.out.println("주문에 아이템 추가하기 : " + itemResult);
			if(itemResult>0) {
				for(OrderItem item : order.getItems()) {
					Cart cart = new Cart();
					cart.setUserNo(order.getUserNo());
					cart.setOptionNo(item.getOptionNo());
					if(checkCart(cart)!=null) {
						cartResult *= removeItem(cart);
						System.out.println("카트 삭제하기 : "+ cartResult);					
					}
					
				}
			}
		}
		return orderResult*itemResult*cartResult;
	}
	public String getPcUrl(KakaoPay pay) throws IOException, ParseException{
		String url = "https://kapi.kakao.com/v1/payment/ready";
		String postParams = "cid=TC0ONETIME"
							+ "&partner_order_id=order1122"
							+ "&partner_user_id="+pay.getUserNo()
							+ "&item_name="+pay.getItemName()
							+ "&quantity="+pay.getQuantity()
							+ "&total_amount="+pay.getTotalAmount()
							+ "&tax_free_amount=0"
							+ "&approval_url=http://localhost:8001/eco/paySuccess"
							+ "&cancel_url=http://localhost:8001/eco/product"
							+ "&fail_url=http://localhost:8001/eco";
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Authorization", "KakaoAK 885b2413809f70be7d0bbf84a70b576c");
		urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		urlConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
		wr.write(postParams.getBytes());
		wr.flush();		
		String responseData = "";
		if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			responseData = readBody(urlConnection.getInputStream());
		}else {
			responseData = readBody(urlConnection.getErrorStream());
		}		
		JSONParser parser = new JSONParser();
		JSONObject element = (JSONObject)parser.parse(responseData);
		String tid = element.get("tid").toString();
		String pcUrl = element.get("next_redirect_pc_url").toString();
		ApproveRequest approveRequest = new ApproveRequest();		
		approveRequest.setUserNo(pay.getUserNo());
		approveRequest.setTid(tid);
		dao.insertReady(sqlSession, approveRequest);		
		return pcUrl;
	}
	private String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
	}
	


	@Override
	public ApproveRequest getRequestParam() {
		return dao.getRequestParam(sqlSession);
	}
	@Override
	public String approvePayment(ApproveRequest approve, Order order) throws IOException, ParseException {
		String url = "https://kapi.kakao.com/v1/payment/approve";
		String param = "cid="+approve.getCid()
						+ "&tid=" + approve.getTid()
						+ "&partner_order_id=" + approve.getPartnerOrderId()
						+ "&partner_user_id=" + approve.getUserNo()
						+ "&pg_token=" + approve.getPgToken();

		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Authorization", "KakaoAK 885b2413809f70be7d0bbf84a70b576c");
		urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		urlConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
		wr.write(param.getBytes());
		wr.flush();
		
		String responseData = "";
		if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			responseData = readBody(urlConnection.getInputStream());
			int orderResult = orderProduct(order);
		}else {
			responseData = readBody(urlConnection.getErrorStream());
		}		
		JSONParser parser = new JSONParser();
		JSONObject element = (JSONObject)parser.parse(responseData);		
		return element.get("item_name").toString();
	}

	@Override
	public ProductOption getProductOption(int optionNo) {
		return dao.getProductOption(sqlSession, optionNo);
	}
	
	@Override
	public ArrayList<Order> getShoppingList(int userNo, PageInfo pi) {		
		int startRow = (pi.getCurrentPage()-1)*pi.getBoardLimit()+1;
		int endRow = pi.getCurrentPage()*pi.getBoardLimit();
		
		HashMap map = new HashMap();
		map.put("startRow", startRow);
		map.put("endRow", endRow);
		map.put("userNo", userNo);
		
		return dao.getShoppingList(sqlSession, map);
	}

	@Override
	public int updateProductCount(int productNo) {
		return dao.updateProductCount(sqlSession, productNo);
	}

	@Override
	public int selectProductCount() {
		return dao.selectProductCount(sqlSession);
	}

	@Override
	public int selectCategoryCount(String category) {
		return dao.selectCategoryCount(sqlSession, category);
	}

	@Override
	public int selectOrderCount(int userNo) {
		return dao.selectOrderCount(sqlSession, userNo);
	}

	@Override
	public int insertReview(ProductReview review) {
		return dao.insertReview(sqlSession, review);
	}

	@Override
	public ArrayList checkReview(int orderNo) {
		return dao.checkReview(sqlSession, orderNo);
	}

	@Override
	public int saveKeyword(String keyword) {
		return dao.saveKeyword(sqlSession, keyword);
	}

	@Override
	@Transactional
	public int checkKeyword(String keyword) {
		int checkResult = dao.checkKeyword(sqlSession, keyword);
		if(checkResult>0) {
			checkResult*=updateKeywordCount(keyword);
		}else {
			checkResult*=saveKeyword(keyword);
		}		
		return checkResult;
	}

	@Override
	public int updateKeywordCount(String keyword) {
		return dao.updateKeywordCount(sqlSession, keyword);
	}

	@Override
	public ArrayList<String> getKeywords(String keyword) {
		return dao.getKeywords(sqlSession, keyword);
	}

	@Override
	public ArrayList<Address> getAddressList(int userNo) {
		return dao.getAddressList(sqlSession, userNo);
	}

	@Override
	public ProductReview getLastReview(int userNo) {
		return dao.getLastReview(sqlSession, userNo);
	}

	@Override
	public int deleteReview(int reviewNo) {
		return dao.deleteReview(sqlSession, reviewNo);
	}

	@Override
	public ArrayList<Order> getOrderList(int userNo) {
		return dao.getOrderList(sqlSession, userNo);
	}

	@Override
	public int addAddress(Address address) {
		return dao.addAddress(sqlSession, address);
	}

	@Override
	public String payResult(String pcUrl) throws IOException {
		return null;
	}


}