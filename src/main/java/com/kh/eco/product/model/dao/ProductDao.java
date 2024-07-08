package com.kh.eco.product.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.eco.product.model.vo.Address;
import com.kh.eco.product.model.vo.ApproveRequest;
import com.kh.eco.product.model.vo.Brand;
import com.kh.eco.product.model.vo.Cart;
import com.kh.eco.product.model.vo.Order;
import com.kh.eco.product.model.vo.OrderItem;
import com.kh.eco.product.model.vo.Product;
import com.kh.eco.product.model.vo.ProductLike;
import com.kh.eco.product.model.vo.ProductOption;
import com.kh.eco.product.model.vo.ProductReview;

@Repository
public class ProductDao {
	public int selectProductCount(SqlSession sqlSession) {
		return sqlSession.selectOne("productMapper.selectProductCount");
	}
		
	public ArrayList<Product> selectProductList(SqlSession sqlSession, Map<String, String> map, RowBounds rowbounds){
		return (ArrayList)sqlSession.selectList("productMapper.selectProductList", map, rowbounds);
	}
	public ArrayList<Product> searchProduct(SqlSession sqlSession, String keyword){
		return (ArrayList)sqlSession.selectList("productMapper.searchProduct", keyword);
	}
	public int addLike(SqlSession sqlSession, ProductLike like) {
		return sqlSession.insert("productMapper.addLike", like);
	}
	public Product selectProduct(SqlSession sqlSession, int productNo) {
		return sqlSession.selectOne("productMapper.selectProduct", productNo);
	}
	public String getPrice(SqlSession sqlSession, int optionNo) {
		return sqlSession.selectOne("productMapper.getPrice", optionNo);
	}
	public ArrayList<String> getImages(SqlSessionTemplate sqlSession, int productNo) {
		return (ArrayList)sqlSession.selectList("productMapper.getImages", productNo);
	}
	public Brand getBrand(SqlSessionTemplate sqlSession, int productNo) {
		return sqlSession.selectOne("productMapper.getBrand", productNo);
	}
	public ProductReview getRate(SqlSessionTemplate sqlSession, int productNo) {
		return sqlSession.selectOne("productMapper.getRate", productNo);
	}
	public ArrayList<ProductReview> reviewList(SqlSessionTemplate sqlSession, int productNo) {
		return (ArrayList)sqlSession.selectList("productMapper.reviewList", productNo);
	}
	public ArrayList<ProductLike> getLikes(SqlSessionTemplate sqlSession, int userNo) {
		return (ArrayList)sqlSession.selectList("productMapper.getLikes", userNo);
	}
	//좋아요 되어있는 상품인지 확인
	public String checkLike(SqlSessionTemplate sqlSession, ProductLike like) {
		return sqlSession.selectOne("productMapper.checkLike", like);
	}
	public int removeLike(SqlSessionTemplate sqlSession, ProductLike like) {
		return sqlSession.delete("productMapper.removeLike", like);
	}
	public ArrayList<Cart> selectCartItems(SqlSessionTemplate sqlSession, int userNo){
		return (ArrayList)sqlSession.selectList("productMapper.selectCartItems", userNo);
	}
	public int updateQty(SqlSessionTemplate sqlSession, Cart cart) {
		return sqlSession.update("productMapper.updateQty", cart);
	}
	public int addCart(SqlSessionTemplate sqlSession, Cart cart) {
		return sqlSession.insert("productMapper.addCart", cart);
	}
	public String checkCart(SqlSessionTemplate sqlSession, Cart cart) {
		return sqlSession.selectOne("productMapper.checkCart", cart);
	}
	public int removeItem(SqlSessionTemplate sqlSession, Cart cart) {
		return sqlSession.delete("productMapper.removeItem", cart);
	}
	public Cart getCartItem(SqlSessionTemplate sqlSession, int optionNo) {
		return sqlSession.selectOne("productMapper.getCartItem", optionNo);
	}
	public int insertOrder(SqlSessionTemplate sqlSession, Order order) {
		return sqlSession.insert("productMapper.insertOrder", order);
	}
	public int insertOrderItem(SqlSessionTemplate sqlSession, Order order) {
		return sqlSession.insert("productMapper.insertOrderItem", order);
	}
	public int insertOrderItem(SqlSessionTemplate sqlSession, OrderItem item) {
		return sqlSession.insert("productMapper.insertOrderItems", item);
	}
	public int insertReady(SqlSessionTemplate sqlSession, ApproveRequest approveRequest) {
		return sqlSession.insert("productMapper.insertReady", approveRequest);
	}
	public ApproveRequest getRequestParam(SqlSessionTemplate sqlSession) {
		return sqlSession.selectOne("productMapper.getRequestParam");
	}
	public ProductOption getProductOption(SqlSessionTemplate sqlSession, int optionNo) {
		return sqlSession.selectOne("productMapper.getProductOption", optionNo);
	}
	public int updateProductCount(SqlSessionTemplate sqlSession, int productNo) {
		return sqlSession.update("productMapper.updateProductCount", productNo);
	}

	public int selectCategoryCount(SqlSessionTemplate sqlSession, String category) {
		return sqlSession.selectOne("productMapper.selectCategoryCount", category);
	}

	public int selectOrderCount(SqlSessionTemplate sqlSession, int userNo) {
		return sqlSession.selectOne("productMapper.selectOrderCount", userNo);
	}

	public int insertReview(SqlSessionTemplate sqlSession, ProductReview review) {
		return sqlSession.insert("productMapper.insertReview", review);
	}

	public ArrayList checkReview(SqlSessionTemplate sqlSession, int orderNo) {
		return (ArrayList)sqlSession.selectList("productMapper.checkReview", orderNo);
	}

	public int saveKeyword(SqlSessionTemplate sqlSession, String keyword) {
		return sqlSession.insert("productMapper.saveKeyword", keyword);
	}

	public int checkKeyword(SqlSessionTemplate sqlSession, String keyword) {
		return sqlSession.selectOne("productMapper.checkKeyword", keyword);
	}

	public int updateKeywordCount(SqlSessionTemplate sqlSession, String keyword) {
		return sqlSession.update("productMapper.updateKeywordCount", keyword);
	}

	public ArrayList<String> getKeywords(SqlSessionTemplate sqlSession, String keyword) {
		return (ArrayList)sqlSession.selectList("productMapper.getKeywords", keyword);
	}

	public ArrayList<Order> getShoppingList(SqlSessionTemplate sqlSession, HashMap map) {
		return (ArrayList)sqlSession.selectList("productMapper.getShoppingList", map);
	}

	public ArrayList<Address> getAddressList(SqlSessionTemplate sqlSession, int userNo) {
		return (ArrayList)sqlSession.selectList("productMapper.getAddressList", userNo);
	}

	public ProductReview getLastReview(SqlSessionTemplate sqlSession, int userNo) {
		return sqlSession.selectOne("productMapper.getLastReview", userNo);
	}

	public int deleteReview(SqlSessionTemplate sqlSession, int reviewNo) {
		return sqlSession.update("productMapper.deleteReview", reviewNo);
	}
	public ArrayList<Order> getOrderList(SqlSessionTemplate sqlSession, int userNo){
		return (ArrayList)sqlSession.selectList("productMapper.getOrderList", userNo);
	}

	public int addAddress(SqlSessionTemplate sqlSession, Address address) {
		return sqlSession.insert("productMapper.addAddress", address);
	}

}