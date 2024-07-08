package com.kh.eco.book.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kh.eco.book.model.service.BookService;
import com.kh.eco.book.model.vo.Book;
import com.kh.eco.book.model.vo.BookReport;
import com.kh.eco.common.model.template.Pagination;
import com.kh.eco.common.model.vo.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {
	
	private final BookService bookService;
	
	private static final String ALADINSERVICEKEY = "ttbrkd_gus_wl1746003";
	
	// 알라딘 api 리스트 메소드
	public ArrayList<Book> selectBookList(int maxResult, String query, int currentPage) {
		try {
		String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
		url += "?TTBKey=" + ALADINSERVICEKEY;
		url += "&Query=" + URLEncoder.encode(query, "UTF-8");
		url += "&MaxResults=" + maxResult;
		url += "&output=JS";
		url += "&Sort=SalesPoint";
		url += "&Version=20131101";
		url += "&Cover=big";
		url += "&start=" + currentPage;
		
		log.info(url);
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		// 결과 받기
		String responseText = br.readLine();
		
		JsonObject totalObj = JsonParser.parseString(responseText).getAsJsonObject();
		JsonArray itemArr = totalObj.getAsJsonArray("item");
		
		ArrayList<Book> bookList = new ArrayList();
		
		for(int i = 0; i < itemArr.size(); i++) {
			JsonObject item = itemArr.get(i).getAsJsonObject();
			
			Book book = new Book();
			
			book.setISBN13(item.get("isbn13").getAsString());
			book.setBookTitle(item.get("title").getAsString());
			book.setBookWriter(item.get("author").getAsString());
			book.setBookImg(item.get("cover").getAsString());
			
			bookList.add(book);
		}
		
		br.close();
		
		urlConnection.disconnect();
		
		
		return bookList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 알라딘 api 상품조회 메소드
	public static Book bookLookUp(String ISBN) throws IOException {
		try {
		String url = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
		url += "?TTBKey=" + ALADINSERVICEKEY;
		url += "&ItemId=" + ISBN;
		url += "&ItemIdType=ISBN13";
		url += "&Cover=Big";
		url += "&Output=JS";
		url += "&Version=20131101";
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		// 결과 받기
		String responseText = br.readLine();
		
		JsonObject totalObj = JsonParser.parseString(responseText).getAsJsonObject();
		JsonArray itemArr = totalObj.getAsJsonArray("item");
		 
		JsonObject item = itemArr.get(0).getAsJsonObject();
		
		Book book = new Book();
		
		book.setISBN13(item.get("isbn13").getAsString());
		book.setBookCategory(item.get("categoryName").getAsString());
		book.setBookContent(item.get("description").getAsString());
		book.setBookDate(item.get("pubDate").getAsString());
		book.setBookLink(item.get("link").getAsString());
		book.setBookPublisher(item.get("publisher").getAsString());
		book.setBookTitle(item.get("title").getAsString());
		book.setBookWriter(item.get("author").getAsString());
		book.setBookImg(item.get("cover").getAsString());
		
		br.close();
		urlConnection.disconnect();
		
		return book;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean checkText(String content, int length) 
	throws Exception{
		if(content.getBytes().length < length) {
			return true;
		}else {
			log.info("값이 너무 큽니다.");
			throw new Exception();
		}
	}
	
	
	// 메인화면 메소드
	@GetMapping("book")
	public String bookMain(@RequestParam(value="cPage", defaultValue="1") int currentPage, 
																		  Model model) {	
		
		List<Book> bookList = selectBookList(20, "환경", currentPage);
		List<Book> countList = bookService.countList();
		
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		for (Book book : countList) {
		    countMap.put(book.getISBN13(), book.getBookCount());
		}
		
		for (Book book : bookList) {
		    String isbn = book.getISBN13();
		    if (countMap.containsKey(isbn)) {
		        int bookCount = countMap.get(isbn);
		        book.setBookCount(bookCount);
		    }
		}
		
		PageInfo pi = Pagination.getPageInfo(200, currentPage, 20, 10);
		
		model.addAttribute("bookList", bookList);
		model.addAttribute("pi", pi);
		
		return "book/book/bookList";
	}
	
	
	// 검색 메소드
	@GetMapping("searchBook.bk")
	public String bookSearch(@RequestParam(value="cPage", defaultValue="1") int currentPage, Model model, String selectBookOption, String searchBookValue) throws IOException {
		
		ArrayList<Book> allList = new ArrayList();
		for(int i = 1; i <= 4; i++) {
			ArrayList<Book> bookList = selectBookList(50, "환경", i);
			for(int n = 0; n < bookList.size(); n++) {
				allList.add(bookList.get(n));
			}
		}
		
		ArrayList<Book> searchList = new ArrayList();
		switch(selectBookOption) {
		case "title" : 
			for(int i = 0; i < allList.size(); i++) {
				if((allList.get(i).getBookTitle()).contains(searchBookValue)) {
					searchList.add(allList.get(i));
				}
			}
			break;
		case "writer" : 
			for(int i = 0; i < allList.size(); i++) {
				if((allList.get(i).getBookWriter()).contains(searchBookValue)) {
					searchList.add(allList.get(i));
				}
			}
			break;
		case "category" : 
			for(int i = 0; i < allList.size(); i++) {
				if((allList.get(i).getBookCategory()).contains(searchBookValue)) {
					searchList.add(allList.get(i));
				}
			}
			break;
		}
		
		List<Book> countList = bookService.countList();
		
		HashMap<String, Integer> countMap = new HashMap<>();
		for (Book book : countList) {
		    countMap.put(book.getISBN13(), book.getBookCount());
		}
		
		for (Book book : searchList) {
		    String isbn = book.getISBN13();
		    if (countMap.containsKey(isbn)) {
		        Integer bookCount = countMap.get(isbn);
		        book.setBookCount(bookCount);
		    }
		}
		
		PageInfo pi = Pagination.getPageInfo(searchList.size(), currentPage, 20, 10);
		// 만약 현재페이지 1 : 0 ~ 19(20)
		// 2 : 20 ~ 39(40)
		// 3 : 40 ~ 59(60)
		// 4 : 60 ~ 79(80)
		int startList = 20 * (currentPage - 1);
		int endList = currentPage * 20;
		if(endList > searchList.size()) {
			endList = searchList.size();
		}
		
		model.addAttribute("bookList", searchList.subList(startList, endList));
		model.addAttribute("pi", pi);
		model.addAttribute("selectBook", selectBookOption);
		model.addAttribute("searchBook", searchBookValue);
		
		return "book/book/bookList";
	}
	
	
	@PostMapping("bookDetail.bk")
	public String bookDetail(String ISBN, Model model, 
							 int count, HttpSession session) throws IOException{
		
			int countBook = 1;
		
			Book book = bookLookUp(ISBN);
			
			int result = bookService.bookDetail(book, count);
			
			if(result > 0) {
				if(count > 0) {
					countBook = bookService.countBook(ISBN);
				}
				book.setBookCount(countBook);
				model.addAttribute("b", book);
				return "book/book/bookDetail";
			} else { 
				session.setAttribute("failBookAlert", "조회 실패");
				return "redirect:book";
			}
	}
	
	// 독후감 게시판 리스트 조회
	@GetMapping("bookReport")
	public String bookReport(@RequestParam(value="cPage", defaultValue="1") int currentPage, Model model) {
		PageInfo pi = Pagination.getPageInfo(bookService.reportCount(), currentPage, 10, 10);
		model.addAttribute("list", bookService.selectReportList(pi));
		model.addAttribute("pi", pi);
		return "book/book/reportList";
	}
	
	// 독후감 게시판 검색목록 리스트 조회
	@GetMapping("reportSearch.bk")
	public ModelAndView searchReportList(@RequestParam(value="cPage", defaultValue="1") int currentPage, 
		   ModelAndView mv, String reportSearchOption, String reportSearchValue, HttpSession session) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("condition", reportSearchOption);
		map.put("keyword", reportSearchValue);
		
		PageInfo pi = Pagination.getPageInfo(bookService.searchReportCount(map), currentPage, 10, 10);
		
		List<BookReport> list = bookService.searchReportList(map, pi);
		
		if(list != null) { // 리스트 조회 성공
			mv.addObject("list", list).addObject("pi", pi).addObject("condition", reportSearchOption).
			addObject("keyword", reportSearchValue).setViewName("book/book/reportList");
		} else { // 리스트 조회 실패
			session.setAttribute("failBookAlert", "조회수 증가 실패");
		}
		return mv;
	}
	
	// 독후감 게시판 작성페이지 포워딩
	@GetMapping("reportEnroll.bk")
	public String reportEnroll() {
		return "book/book/reportEnrollForm";
	}
	
	// 독후감 게시판 작성
	@PostMapping("reportEnrollForm.bk")
	public String reportEnrollForm(BookReport bookReport, HttpSession session) throws Exception {
		
		checkText(bookReport.getBookReportTitle(), 33);
		checkText(bookReport.getBookReportContent(), 1333);
		
		if(bookService.reportEnrollForm(bookReport) > 0) { // 작성 성공
			return "redirect:bookReport";
		} else { // 작성 실패
			session.setAttribute("failBookAlert", "게시글 작성 실패");
			return "redirect:bookReport";
		}
		
	}
	 
	// 독후감 게시판 상세조회
	@GetMapping("reportDetail.bk")
	public String reportDetail(int rno, Model model, HttpSession session) {
		
		if(bookService.countReport(rno) > 0) { // 조회수 증가 성공
			BookReport bookReport = bookService.reportDetail(rno);
			if(bookReport != null) { // 상세조회 성공
				model.addAttribute("br", bookReport);
			} else { // 상세조회 실패
				session.setAttribute("failBookAlert", "상세조회 실패");
			}
		} else { // 조회수 증가 실패
			session.setAttribute("failBookAlert", "상세조회 실패");
		}
		
		return "book/book/reportDetail";
	}
	
	// 독후감 게시글 수정 포워딩
	@GetMapping("reportUpdate.bk")
	public String reportUpdate(int reportNo, Model model) {
		
		model.addAttribute("br", bookService.reportDetail(reportNo));
		
		return "book/book/reportEnrollForm";
	}
	
	// 독후감 게시글 수정
	@PostMapping("reportUpdateForm.bk")
	public String reportUpdateForm(BookReport bookReport, HttpSession session) throws Exception {
		
		checkText(bookReport.getBookReportTitle(), 33);
		checkText(bookReport.getBookReportContent(), 1333);
		
		if(bookService.reportUpdateForm(bookReport) > 0) { // 수정 성공
			session.setAttribute("successBookAlert", "게시글 수정 성공");
			return "redirect:bookReport";
		} else { // 수정 실패
			session.setAttribute("failBookAlert", "게시글 수정 실패");
			return "redirect:bookReport";
		}
	}
	
	// 독후감 게시글 삭제
	@PostMapping("reportDelete.bk")
	public String reportDelete(int reportNo, HttpSession session) {
		
		if(bookService.reportDelete(reportNo) > 0) { // 삭제 성공
			session.setAttribute("successBookAlert", "게시글 삭제 성공");
			return "redirect:bookReport";
		} else { // 삭제 실패
			session.setAttribute("failBookAlert", "게시글 삭제 실패");
			return "redirect:bookReport";
		}
	}
	
	// 독후감 게시글 신고
	@PostMapping("reportBlack.bk")
	public String reportBlack(int reportNo, int userNo, HttpSession session) {
		
		HashMap<String, Object> map = new HashMap();
		map.put("reportNo", reportNo);
		map.put("userNo", userNo);
		
		if(bookService.reportBlack(map) > 0) {
			session.setAttribute("successBookAlert", "게시글 신고 성공");
			return "redirect:bookReport";
		} else { // 신고 실패
			session.setAttribute("failBookAlert", "게시글 신고 실패");
			return "redirect:bookReport";
		}
	}
	
	// 도서 마이페이지 포워딩
	@GetMapping("bookMyPage")
	public String BookMyPage() {
		return "book/mypage/bookMyPage";
	}
	
	// 독후감 게시판 마이페이지 포워딩
	@GetMapping("reportMyPage")
	public String ReportMyPage() {
		return "book/mypage/reportMyPage";
	}

	
	// 신고게시판 포워딩
	@GetMapping("adminReport")
	public String adminReport() {
		return "book/admin/adminReport";
	}
	
	
}
