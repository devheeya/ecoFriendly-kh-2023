package com.kh.eco.book.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class Book {
	
	private String ISBN13;
	private int bookCount;
	private String bookTitle;
	private String bookWriter;
	private String bookImg;
	private String bookPublisher;
	private String bookContent;
	private String bookDate;
	private String bookCategory;
	private String bookLink;
	private String bookReply;
	
}


