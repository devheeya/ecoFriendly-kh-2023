package com.kh.eco.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kh.eco.product.model.service.ProductService;
import com.kh.eco.product.model.vo.Cart;
import com.kh.eco.product.model.vo.ProductLike;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AjaxProductController {

	private final ProductService productService;
	
	@RequestMapping(value = "product.like", produces="text/html; charset=UTF-8")
	public String like(ProductLike like) {
		if(checkLike(like).equals("Y")) {
			return productService.removeLike(like)==1? "removed" : "remove failed";
		}else {
			int result = productService.addLike(like);
			return result>0? "added":"failed to add like";		
		}
	}
	@GetMapping(value="check.like", produces="text/html; charset=UTF-8")
	public String checkLike(ProductLike like) {
		return productService.checkLike(like);
	}
	@GetMapping(value = "delete.like", produces="text/html; charset=UTF-8")
	public String disLike(ProductLike like) {
		return productService.removeLike(like)>0? "removed" : "remove failed";
	}
	public int removeLike(ProductLike like) {
		return productService.removeLike(like);
	}	
	@GetMapping(value ="getPrice", produces="text/html; charset=UTF-8")
	public String getPrice(int optionNo) {
		String price = productService.getPrice(optionNo);
		return price;
	}
	@GetMapping(value ="product.review", produces="application/json; charset=UTF-8")
	public String ajaxReviewList(int productNo, Model model) {
		return new Gson().toJson(productService.reviewList(productNo));
	}
	@GetMapping(value="getLikes.pr", produces="application/json; charset=UTF-8")
	public String ajaxGetLikes(int userNo) {
		return new Gson().toJson(productService.getLikes(userNo));
	}
	@PostMapping(value="update.cart", produces="html/text; charset=UTF-8")
	public String updateQty(Cart cart) {
		return productService.updateQty(cart)>0? "success":"fail";
	}

	@PostMapping(value="add.cart", produces="html/text; charset=UTF-8")
	public String addCart(Cart cart) {
		String result = productService.checkCart(cart);
		if(result == null) {//장바구니에 아이템이 존재하지 않을 때
			return productService.addCart(cart)>0? "added":"failed";
		}else {
			return result;
		}
	}

	@PostMapping(value="remove.item", produces="html/text; charset=UTF-8")
	public String removeItem(Cart cart) {
		if(productService.removeItem(cart)>0) {
			return "completed";
		}else {
			return "failed";
		}
	}

	@GetMapping(value="check.review", produces="application/json; charset=UTF-8")
	public String checkReview(int orderNo) {
		return new Gson().toJson(productService.checkReview(orderNo));
	}
	

	@GetMapping(value = "getKeywords", produces="application/json; charset=UTF-8")
	public String getKeywords(String keyword) {
		return new Gson().toJson(productService.getKeywords(keyword));
	}
	@GetMapping(value = "addressList", produces="application/json; charset=UTF-8")
	public String getAddressList(int userNo) {
		return new Gson().toJson(productService.getAddressList(userNo));
	}
}
