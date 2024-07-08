package com.kh.eco.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PageInfo {
	
	private int listCount; // 게시글 전체 개수
	private int currentPage; // 현재 페이지
	private int boardLimit; // 한 페이지에서 보이는 게시글의 개수
	private int pageLimit; // 페이징버튼의 개수
	
	private int maxPage; // 마지막 페이지
	private int startPage; // 페이징 버튼의 시작
	private int endPage; // 페이징 버튼의 끝
	
}
