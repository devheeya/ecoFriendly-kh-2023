package com.kh.eco.book.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.eco.book.model.vo.Book;
import com.kh.eco.book.model.vo.BookReply;
import com.kh.eco.book.model.vo.BookReport;

@Repository
public class BookDao {
	
	public ArrayList<Book> countList(SqlSessionTemplate sqlSession){
		return (ArrayList)sqlSession.selectList("bookMapper.countList");
	}
	
	public int insertBook(SqlSessionTemplate sqlSession, String ISBN) {
		return sqlSession.insert("bookMapper.insertBook", ISBN);
	}
	
	public int increaseBook(SqlSessionTemplate sqlSession, String ISBN) {
		return sqlSession.update("bookMapper.increaseBook", ISBN);
	}
	
	public int countBook(SqlSessionTemplate sqlSession, String ISBN) {
		return sqlSession.selectOne("bookMapper.countBook", ISBN);
	}
	
	public int insertBookMark(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.insert("bookMapper.insertBookMark", map);
	}
	
	public int removeBookMark(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.delete("bookMapper.removeBookMark", map);
	}
	
	public int ajaxSelectBookMark(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.selectOne("bookMapper.selectBookMark", map);
	}
	
	public int ajaxInsertBookReply(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.insert("bookMapper.ajaxInsertBookReply", map);
	}
	
	public int ajaxSelectBookReplyCount(SqlSessionTemplate sqlSession, String ISBN13) {
		return sqlSession.selectOne("bookMapper.ajaxSelectBookReplyCount", ISBN13);
	}
	
	public ArrayList<BookReply> ajaxSelectBookReply(SqlSessionTemplate sqlSession, String ISBN13, RowBounds rowBounds){
		return (ArrayList)sqlSession.selectList("bookMapper.ajaxSelectBookReply", ISBN13, rowBounds);
	}
	
	public int ajaxDeleteBookReply(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.delete("bookMapper.ajaxDeleteBookReply", map);
	}
	
	public int ajaxDeleteBookEco(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.delete("bookMapper.ajaxDeleteBookEco", map);
	}
	
	public int reportCount(SqlSessionTemplate sqlSession) {
		return sqlSession.selectOne("bookMapper.reportCount");
	}
	
	public ArrayList<BookReport> selectReportList(SqlSessionTemplate sqlSession, RowBounds rowBounds){
		return (ArrayList)sqlSession.selectList("bookMapper.selectReportList", null, rowBounds);
	}
	
	public int searchReportCount(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.selectOne("bookMapper.searchReportCount", map);
	}
	
	public ArrayList<BookReport> searchReportList(SqlSessionTemplate sqlSession, HashMap map, RowBounds rowBounds){
		return (ArrayList)sqlSession.selectList("bookMapper.searchReportList", map, rowBounds);
	}
	
	public int reportEnrollForm(SqlSessionTemplate sqlSession, BookReport bookReport) {
		return sqlSession.insert("bookMapper.reportEnrollForm", bookReport);
	}
	
	public int countReport(SqlSessionTemplate sqlSession, int rno) {
		return sqlSession.update("bookMapper.countReport", rno);
	}
	
	public BookReport reportDetail(SqlSessionTemplate sqlSession, int rno) {
		return sqlSession.selectOne("bookMapper.reportDetail", rno);
	}
	
	public int reportUpdateForm(SqlSessionTemplate sqlSession, BookReport bookReport) {
		return sqlSession.update("bookMapper.reportUpdateForm", bookReport);
	}
	
	public int reportDelete(SqlSessionTemplate sqlSession, int reportNo) {
		return sqlSession.update("bookMapper.reportDelete", reportNo);
	}
	
	public int reportBlack(SqlSessionTemplate sqlSession, HashMap map) {
		return sqlSession.insert("bookMapper.reportBlack", map);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
