package com.kh.eco.product.model.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.eco.product.model.dao.ProductDao;
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

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao dao;
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public ArrayList<Product> selectProductList() {
		return dao.selectProductList(sqlSession);
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
			+ "&partner_order_id=order1122"//가맹점 주문 번호 문자열
			+ "&partner_user_id="+pay.getUserNo() //가맹점 회원id 문자열
			+ "&item_name="+pay.getItemName() //상품명 문자열
			+ "&quantity="+pay.getQuantity()//상품갯수
			+ "&total_amount="+pay.getTotalAmount()
			+ "&tax_free_amount=0"
			+ "&approval_url=http://localhost:8001/eco/paySuccess"
			+ "&cancel_url=http://localhost:8001/eco/product"
			+ "&fail_url=http://localhost:8001/eco"					
			;
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
		System.out.println("tid : "+tid);
		String pcUrl = element.get("next_redirect_pc_url").toString();
		ApproveRequest approveRequest = new ApproveRequest();
		
		approveRequest.setUserNo(pay.getUserNo());
		approveRequest.setTid(tid);
		
		int result = dao.insertReady(sqlSession, approveRequest);
		if(result>0) System.out.println("tid값 인서트 성공");
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
	public String payResult(String pcUrl) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
			//responseCode 200 성공~~
			int orderResult = orderProduct(order);
			if(orderResult>0) {
				System.out.println("DB도 잘 다녀옴");
			}
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
}
