package com.kh.eco.book.model.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.book.model.dao.BookDao;
import com.kh.eco.book.model.vo.Book;
import com.kh.eco.book.model.vo.BookReply;
import com.kh.eco.book.model.vo.BookReport;
import com.kh.eco.book.model.vo.BookReportReply;
import com.kh.eco.common.model.vo.PageInfo;

import lombok.RequiredArgsConstructor;

@EnableTransactionManagement
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
	
	private final BookDao bookDao;
	
	private final SqlSessionTemplate sqlSession;
	
	// 페이징처리를 위한 offset 만들기
	private RowBounds createRowBounds(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return rowBounds;
	}

	
	@Override
	public ArrayList<Book> countList() {
		return (ArrayList<Book>) bookDao.countList(sqlSession);
	}

	@Override
	public int bookDetail(Book book, int count) {
		if(count == 0) { 
			return bookDao.insertBook(sqlSession, book);
		} else { 
			return bookDao.increaseBook(sqlSession, book.getISBN13());
		}
	}

	@Override
	public int countBook(String ISBN) {
		return bookDao.countBook(sqlSession, ISBN);
	}

	@Override
	public Map<String, Object> 
	bookMarkMenu(Map<String, Object> map, String className) {
		Map<String, Object> resultMap = 
					new HashMap<String, Object>();
		if(className.equals("bookmark")) {
			className = "bookmark abled";
			resultMap.put("result", 
						  bookDao.insertBookMark(sqlSession, map));
			resultMap.put("className", className);
		} else if(className.equals("bookmark abled")) {
			className = "bookmark";
			resultMap.put("result", 
					      bookDao.removeBookMark(sqlSession, map));
			resultMap.put("className", className);
		}
		return resultMap;
	}

	@Override
	public int selectBookMark(Map map) {
		return bookDao.ajaxSelectBookMark(sqlSession, map);
	}

	@Override
	public int insertBookReply(Map map) {
		return bookDao.insertBookReply(sqlSession, map);
	}

	@Override
	public int ajaxSelectBookReplyCount(String ISBN13) {
		return bookDao.ajaxSelectBookReplyCount(sqlSession, ISBN13);
	}
	
	@Override
	public List<BookReply> ajaxSelectBookReply(String ISBN13, PageInfo pi) {
		return bookDao.ajaxSelectBookReply(sqlSession, ISBN13, createRowBounds(pi));
	}
	
	@Override
	@Transactional("transactionManager")
	public int ajaxDeleteBookReply(HashMap map) {
		return bookDao.ajaxDeleteBookEco(sqlSession, map) + bookDao.ajaxDeleteBookReply(sqlSession, map);
	}

	@Override
	public int reportCount() {
		return bookDao.reportCount(sqlSession);
	}

	@Override
	public List<BookReport> selectReportList(PageInfo pi) {
		// (pi.getCurrentPage() - 1) * pi.getBoardLimit() ~ pi.getBoardLimit() * pi.getCurrentPage()
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startLimit", (pi.getCurrentPage() - 1) * pi.getBoardLimit());
		map.put("endLimit", pi.getBoardLimit() * pi.getCurrentPage());
		
		return bookDao.selectReportList(sqlSession, map);
	}
	
	@Override
	public int searchReportCount(Map map) {
		return bookDao.searchReportCount(sqlSession, map);
	}

	@Override
	public List<BookReport> searchReportList(Map map, PageInfo pi) {
		return bookDao.searchReportList(sqlSession, map, createRowBounds(pi));
	}
	
	@Override
	public int reportEnrollForm(BookReport bookReport) {
		return bookDao.reportEnrollForm(sqlSession, bookReport);
	}
	
	@Override
	public int countReport(int reportNo) {
		return bookDao.countReport(sqlSession, reportNo);
	}

	@Override
	public BookReport reportDetail(int reportNo) {
		return bookDao.reportDetail(sqlSession, reportNo);
	}
	
	@Override
	public int reportUpdateForm(BookReport bookReport) {
		return bookDao.reportUpdateForm(sqlSession, bookReport);
	}
	
	@Override
	public int reportDelete(int reportNo) {
		return bookDao.reportDelete(sqlSession, reportNo);
	}
	
	@Override
	public int reportBlack(HashMap map) {
		return bookDao.reportBlack(sqlSession, map);
	}
	
	@Override
	public int ajaxSelectReportReplyCount(int reportNo) {
		return bookDao.ajaxSelectReportReplyCount(sqlSession, reportNo);
	}
	
	@Override
	public List<BookReportReply> ajaxSelectReportReply(int reportNo, PageInfo pi) {
		return bookDao.ajaxSelectReportReply(sqlSession, reportNo, createRowBounds(pi));
	}

	@Override
	public int ajaxInsertReportReply(HashMap map) {
		return bookDao.ajaxInsertReportReply(sqlSession, map);
	}

	@Override
	public int ajaxUpdateReportReply(HashMap map) {
		return bookDao.ajaxUpdateReportReply(sqlSession, map);
	}

	@Override
	public int ajaxDeleteReportReply(int replyNo) {
		return bookDao.ajaxDeleteReportReply(sqlSession, replyNo);
	}

	@Override
	public int ajaxReplyBlack(HashMap map) {
		return bookDao.ajaxReplyBlack(sqlSession, map);
	}

	@Override
	public int bookmarkCountMyPage(int userNo) {
		return bookDao.bookmarkCountMyPage(sqlSession, userNo);
	}

	@Override
	public List<String> bookmarkMyPage(int userNo, PageInfo pi) {
		return bookDao.bookmarkMyPage(sqlSession, userNo, createRowBounds(pi));
	}

	@Override
	public int bookReplyCountMyPage(int userNo) {
		return bookDao.bookReplyCountMyPage(sqlSession, userNo);
	}

	@Override
	public List<BookReply> bookReplyMyPage(int userNo, PageInfo pi) {
		return (List)bookDao.bookReplyMyPage(sqlSession, userNo, createRowBounds(pi));
	}

	@Override
	public int reportCountMyPage(int userNo) {
		return bookDao.reportCountMyPage(sqlSession, userNo);
	}

	@Override
	public List<BookReport> reportMyPage(int userNo, PageInfo pi) {
		return bookDao.reportMyPage(sqlSession, userNo, createRowBounds(pi));
	}

	@Override
	public int reportReplyCountMyPage(int userNo) {
		return bookDao.reportReplyCountMyPage(sqlSession, userNo);
	}

	@Override
	public List<BookReportReply> reportReplyMyPage(int userNo, PageInfo pi) {
		return bookDao.reportReplyMyPage(sqlSession, userNo, createRowBounds(pi));
	}

	@Override
	public int adminReportBlackCount() {
		return bookDao.adminReportBlackCount(sqlSession);
	}

	@Override
	public List<BookReport> adminReportBlack(PageInfo pi) {
		return bookDao.adminReportBlack(sqlSession, createRowBounds(pi));
	}

	@Override
	public int adminReportReplyBlackCount() {
		return bookDao.adminReportReplyBlackCount(sqlSession);
	}

	@Override
	public List<BookReportReply> adminReportReplyBlack(PageInfo pi) {
		return bookDao.adminReportReplyBlack(sqlSession, createRowBounds(pi));
	}

	@Override
	@Transactional("transactionManager")
	public int deleteReportBlack(int[] arr) {
		int result = 0;
		for(int i = 0; i < arr.length; i++) {
			result += bookDao.deleteReportBlack(sqlSession, arr[i]);
		}
		return result;
	}

	@Override
	@Transactional("transactionManager")
	public int deleteReplyBlack(int[] arr) {
		int result = 0;
		for(int i = 0; i < arr.length; i++) {
			result += bookDao.deleteReplyBlack(sqlSession, arr[i]);
		}
		return result;
	}


	
	
	
	

}
