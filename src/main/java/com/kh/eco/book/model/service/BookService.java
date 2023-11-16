package com.kh.eco.book.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.eco.book.model.vo.Book;

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
	public int selectBookMark(HashMap map);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
