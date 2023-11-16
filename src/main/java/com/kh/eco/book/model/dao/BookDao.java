package com.kh.eco.book.model.dao;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.eco.book.model.vo.Book;

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

}
