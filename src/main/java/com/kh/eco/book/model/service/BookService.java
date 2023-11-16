package com.kh.eco.book.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.eco.book.model.vo.Book;
import com.kh.eco.book.model.vo.BookReply;
import com.kh.eco.common.model.vo.PageInfo;

public interface BookService {
	

	// 도서정보리스트 조회수 조회
	public ArrayList<Book> countList();
	
	// 도서정보 조회수 추가
	public int insertBook(String ISBN);
	
	// 도서정보 조회수 증가
	public int increaseBook(String ISBN);
	
	// 도서정보 조회수 조회
	public int countBook(String ISBN);
	
	// 도서 북마크 추가
	public int insertBookMark(HashMap map);
	
	// 도서 북마크 제거
	public int removeBookMark(HashMap map);
	
	// 도서 북마크 추가
	public int ajaxSelectBookMark(HashMap map);
	
	// 도서 한줄평 등록
	public int ajaxInsertBookReply(HashMap map);
	
	// 도서 한줄평 개수 조회
	public int ajaxSelectBookReplyCount(String ISBN13);
	
	// 도서 한줄평 조회
	public ArrayList<BookReply> ajaxSelectBookReply(String ISBN13, PageInfo pi);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
