package com.kh.eco.product.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.common.model.template.Pagination;
import com.kh.eco.common.model.vo.PageInfo;
import com.kh.eco.product.model.service.ProductService;
import com.kh.eco.product.model.vo.Address;
import com.kh.eco.product.model.vo.ApproveRequest;
import com.kh.eco.product.model.vo.Cart;
import com.kh.eco.product.model.vo.KakaoPay;
import com.kh.eco.product.model.vo.Order;
import com.kh.eco.product.model.vo.OrderCart;
import com.kh.eco.product.model.vo.ProductLike;
import com.kh.eco.product.model.vo.ProductOption;
import com.kh.eco.product.model.vo.ProductReview;
import com.kh.eco.user.model.vo.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	@GetMapping("product")
	public String productHome(@RequestParam(value="cPage", defaultValue="1") int currentPage,
								@RequestParam(value="orderBy", defaultValue="latest") String orderBy, 
								@RequestParam(value="category", defaultValue="all") String category,
								String keyword, Model model) {
		int count = category.equals("all")? productService.selectProductCount() 
										  : productService.selectCategoryCount(category);
		PageInfo pi = Pagination.getPageInfo(count, currentPage, 6, 5);
		model.addAttribute("pi", pi);						
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderBy", orderBy);
		map.put("category", category);
		map.put("keyword", keyword);		
		if(keyword != null) productService.checkKeyword(keyword);

		model.addAttribute("map", map);
		model.addAttribute("productList", productService.selectProductList(map, pi));		
		return "product/productHome";
	}
	
	@GetMapping("product.detail")
	public String showDetail(ProductLike like, Model model) {
		String likeCheck = like.getUserNo() !=0 ? productService.checkLike(like) : "N";
		int productNo = like.getProductNo();
		productService.updateProductCount(productNo);
		model.addAttribute("like", likeCheck);
		model.addAttribute("p", productService.selectProduct(productNo));
		model.addAttribute("images", productService.getImages(productNo));
		model.addAttribute("brand", productService.getBrand(productNo));
		model.addAttribute("review", productService.getRate(productNo));
		return "product/productDetail";
	}
	

	@GetMapping("cart")
	public String myCart(int userNo, Model model) {
		List<Cart> cartItems = productService.selectCartItems(userNo);
		model.addAttribute("cartItems", cartItems);
		return "product/cart";
	}
	
	
	@PostMapping("listOrderForm")
	public String orderListForm(OrderCart orderCart, Model model) {		
		List<Cart> itemList = orderCart.getItemList();
		model.addAttribute("items",orderCart);
		int numOfItem = itemList.size();
		int totalPrice = 0;
		for(int i =0 ; i<numOfItem;i++) {
			totalPrice+= itemList.get(i).getPrice()*itemList.get(i).getQty();
		}
		model.addAttribute("shipping", (totalPrice>=40000)? true : false);
		model.addAttribute("numOfItem", numOfItem);
		model.addAttribute("totalPrice", totalPrice);
		return "product/orderForm";
	}
	@GetMapping("listOrderForm")
	public String orderListForm(Cart item
								, Model model) {		
		ProductOption option = productService.getProductOption(item.getOptionNo());
		item.setOptionName(option.getOptionName());
		item.setPrice(option.getPrice());
		OrderCart orderCart = new OrderCart();
		orderCart.getItemList().add(item);
		model.addAttribute("items",orderCart);
		
		int totalPrice = item.getPrice()*item.getQty();
		model.addAttribute("shipping", (totalPrice>=40000)? true : false);
		model.addAttribute("numOfItem", 1);
		model.addAttribute("totalPrice", totalPrice);
		return "product/orderForm";
	}
	
	@PostMapping("product.order")
	public String orderProduct(Order order
							   , KakaoPay pay
							   , Model model
							   , HttpSession session) throws IOException, ParseException {
		session.setAttribute("order", order);
		model.addAttribute("pcUrl", productService.getPcUrl(pay));
		return "product/payReady";
	}
	
	@GetMapping("shoppingList")
	public String getShoppingList(@RequestParam(value="cPage", defaultValue="1") 
								int currentPage, 
								int userNo, Model model) {
		PageInfo pi = Pagination.getPageInfo(productService.selectOrderCount(userNo), currentPage, 4, 5);
		model.addAttribute("pi", pi);	
		List<Order> orders = productService.getShoppingList(userNo, pi);
		for(Order o : orders) {
			int totalPrice =0;
			o.setItemQty(o.getOrderDetail().size());
			for(Cart c : o.getOrderDetail()) {
				totalPrice+=c.getPrice()*c.getQty();
			}
			o.setTotalPrice(totalPrice);
		}
		model.addAttribute("orders", orders);
		return "product/shoppingList";
	}
	
	@GetMapping("myshopping")
	public String myPage(int userNo, Model model) {
		HashMap map = new HashMap();
		model.addAttribute("likeList", productService.getLikes(userNo));
		model.addAttribute("review", productService.getLastReview(userNo));
		model.addAttribute("orderList", productService.getOrderList(userNo));
		return "product/myshopping";
	}
	
	
	@GetMapping("paySuccess")
	public String getSuccessPage(String pg_token
								, Model model
								, HttpSession session) throws IOException, ParseException {
		ApproveRequest approveRequest = productService.getRequestParam();
		approveRequest.setPgToken(pg_token);
		model.addAttribute("itemName", productService.approvePayment(approveRequest
																	, (Order)session.getAttribute("order")));
		session.removeAttribute("order");
		return "product/paySuccess";
	}
	
	@GetMapping("payReady")
	public String payReady() {
		return "product/payReady";
	}
	
	@GetMapping("reviewForm")
	public String reviewForm() {
		return "product/reviewForm";
	}
	
	@PostMapping("insert.review")
	public String insertReview(ProductReview review, MultipartFile upfile, 
								HttpSession session ,Model model) {
		String originName = upfile.getOriginalFilename();
		if(!originName.equals("")) {
			review.setOriginName(originName);
			review.setChangeName(saveFile(upfile, session));
		}
		int result = productService.insertReview(review);
		String alertMsg = result>0? "후기 등록 성공":"후기 등록 실패";
		model.addAttribute("alertMsg", alertMsg);
		return "redirect:/";
	}
	
	
	public String saveFile(MultipartFile upfile, HttpSession session) {
		String originName = upfile.getOriginalFilename();
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		int ranNum = (int)Math.random()*90000 + 10000;
		String ext =originName.substring(originName.lastIndexOf("."));
		String changeName = currentTime + ranNum + ext;
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
		try {
			upfile.transferTo(new File(savePath+changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return "resources/uploadFiles/"+changeName;
	}
	
	@GetMapping("delete.review")
	public String deleteReview(int reviewNo, HttpSession session) {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser != null) {
			productService.deleteReview(reviewNo);
			return "redirect:/myshopping?userNo="+loginUser.getUserNo();
		}else {
			return "redirect:/";
		}
	}
	
	@PostMapping("newAddress")
	public String newAddress(Address address, HttpSession session) {
		User loginUser = ((User)session.getAttribute("loginUser"));
		address.setUserNo(loginUser.getUserNo());
		productService.addAddress(address);
		return "redirect:/product";
	}

	
	
}