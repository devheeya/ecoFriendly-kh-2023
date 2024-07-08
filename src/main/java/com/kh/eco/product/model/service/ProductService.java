package com.kh.eco.product.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.google.gson.JsonElement;
import com.kh.eco.common.model.vo.PageInfo;
import com.kh.eco.product.model.vo.Address;
import com.kh.eco.product.model.vo.ApproveRequest;
import com.kh.eco.product.model.vo.Brand;
import com.kh.eco.product.model.vo.Cart;
import com.kh.eco.product.model.vo.KakaoPay;
import com.kh.eco.product.model.vo.Order;
import com.kh.eco.product.model.vo.Product;
import com.kh.eco.product.model.vo.ProductLike;
import com.kh.eco.product.model.vo.ProductOption;
import com.kh.eco.product.model.vo.ProductReview;

public interface ProductService {
	
	int selectProductCount();
	int selectCategoryCount(String category);
	ArrayList<Product> selectProductList(Map<String, String> map, PageInfo pi);
	ArrayList<Product> searchProduct(String keyword);
	
	int addLike(ProductLike like);
	Product selectProduct(int productNo);
	String getPrice(int optionNo);
	ArrayList<String> getImages(int productNo);
	Brand getBrand(int productNo);
	ProductReview getRate(int productNo);
	ArrayList<ProductReview> reviewList(int productNo);
	ArrayList<ProductLike> getLikes(int userNo);
	String checkLike(ProductLike like);
	int removeLike(ProductLike like);
	ArrayList<Cart> selectCartItems(int userNo);
	int updateQty(Cart cart);
	int addCart(Cart cart);
	String checkCart(Cart cart);
	int removeItem(Cart cart);
	Cart getCartItem(int optionNo);
	int orderProduct(Order order);
	String getPcUrl(KakaoPay pay) throws IOException, ParseException;
	String payResult(String pcUrl) throws IOException;
	ApproveRequest getRequestParam();
	String approvePayment(ApproveRequest approve, Order order) throws IOException, ParseException;
	ProductOption getProductOption(int optionNo);
	
	int selectOrderCount(int userNo);
	ArrayList<Order> getShoppingList(int userNo, PageInfo pi);
	int updateProductCount(int productNo);
	int insertReview(ProductReview review);
	ArrayList checkReview(int orderNo);
	int saveKeyword(String keyword);
	int checkKeyword(String keyword);
	int updateKeywordCount(String keyword);
	ArrayList<String> getKeywords(String keyword);
	ArrayList<Address> getAddressList(int userNo);
	ProductReview getLastReview(int userNo);
	int deleteReview(int reviewNo);
	ArrayList<Order> getOrderList(int userNo);
	int addAddress(Address address);


}