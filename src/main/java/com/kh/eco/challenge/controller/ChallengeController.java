package com.kh.eco.challenge.controller;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.challenge.model.service.ChallengeService;
import com.kh.eco.challenge.model.vo.Challenge;
import com.kh.eco.common.model.template.Pagination;
import com.kh.eco.common.model.vo.Activity;
import com.kh.eco.common.model.vo.PageInfo;
import com.kh.eco.event.model.service.EventService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChallengeController {

	
	private final ChallengeService challengeService;
	
	// 전체 리스트 조회
	@GetMapping("challenge")
	public String selectChallengeList(@RequestParam(value="currentPage", defaultValue="1")int currentPage, Model model) throws IOException {
		
		/*내 답안 ==> private이라서 setter,getter로 바로 못 받음
		 * PageInfo pi = new PageInfo();
		 * pi.setListCount(challengeService.countChallengeList())
		 */
		
		PageInfo pi = Pagination.getPageInfo( 
											challengeService.countChallengeList(),//  listCount
											currentPage,
											4, // boardLimit
											5 // pageLimit
											);
		// System.out.println(challengeService.countChallengeList());
		
		// 2. 페이징정보 불러오기 : model, modelAndView, session 셋 중 하나
		model.addAttribute("list", challengeService.selectChallengeList(pi)); // list 정보
		model.addAttribute("pi", pi); // list개수에 따른 페이징 정보
		//System.out.println(challengeService.selectChallengeList(pi));
		
		// 3. 화면 포워딩하기   WEB-INF/views/        "요기"           .jsp
		return "challenge/challengeListView"; // vs "redirect:challenge"
		
	}
	
	// 검색결과  조회
	@GetMapping("search.condition")
	public String selectChallengeSearch(@RequestParam(value="currentPage", defaultValue="1")int currentPage, Model model, String condition, String keyword) {
		
		// condition과 keyword 한 쌍으로 담기
		HashMap<String, String> map = new HashMap();
		map.put("condition", condition);
		map.put("keyword", keyword);

		System.out.println(condition);
		PageInfo pi = Pagination.getPageInfo( 
				challengeService.countSearchList(map),// 검색결과수 구하기
				currentPage,
				4,
				5
				);
		
		System.out.println("검색결과수 : " + challengeService.countSearchList(map));
		
		// 2. 페이징정보 불러오기 : model, modelAndView, session 셋 중 하나
		model.addAttribute("condition", condition);
		model.addAttribute("keyword", keyword);
		model.addAttribute("list", challengeService.selectSearchList(map, pi));
		model.addAttribute("pi", pi);
		
		System.out.println("검색결과리스트 : " + challengeService.selectSearchList(map, pi));
		
		// 화면 redirect? 포워딩?
		//return "redirect:challenge"; 
		return "challenge/challengeListView";

	}
	


	// 게시글 정렬결과 조회
	
	@GetMapping("search.status")
	public String selectChallengeStatus(@RequestParam(value="currentPage", defaultValue="1")
														 int currentPage, Model model, String status) {

		HashMap<String, String> map = new HashMap(); 
		map.put("status", status);

		PageInfo pi = Pagination.getPageInfo( 
				challengeService.countChallengeStatus(map),
				currentPage,
				4,
				5
				);
		
		model.addAttribute("status", status);
		model.addAttribute("list", challengeService.selectChallengeStatus(map, pi));	
		model.addAttribute("pi", pi);

		return "challenge/challengeListView";
	}

	
	
	
	
	// challengeEnrollForm으로 가는 매핑메서드
	@RequestMapping("enrollForm.ch")
	public String challengeEnrollForm() {
		
		return "challenge/challengeEnrollForm";
		
	}
	
	// insert완료시 목록뷰로 감
	@PostMapping("insert.ch")
	public String insertChallenge(Challenge c, 
												MultipartFile upfile,
												HttpSession session,
												Model model) {

		  System.out.println(c); 
		  System.out.println(upfile);

		if( !upfile.getOriginalFilename().equals("") ) {
			
			//System.out.println("upfile은 null이 아니야");
			// challenge에 업로드한 파일 원본명/새이름 세팅
			c.setOriginName(upfile.getOriginalFilename());
			c.setChangeName(saveFile(upfile, session));// 업로드한 파일과 해당 세션을 가지고 새이름을 세팅
			// changeName을 get하면 나오는 값임(이미 업로드한 파일, 세션, 저장경로 등이 들어가있음)
		} 
		
		if(challengeService.insertChallenge(c) > 0) {
			
			session.setAttribute("alertMsg", "게시글 작성 성공!!!!");
			//System.out.println("등록된 Challenge정보 : " + challengeService.insertChallenge(c));
			
		} else {
			
			System.out.println("게시글 작성 실패!!");
			
		}
		
		return "redirect:/challenge";
		
	}
	
	
	
	// 파일원본명, 서버업로드할 경로+바뀐이름 2개를 challenge에 담는 메서드
	// ex. 'bonobono.jsp' => 2023111610383.jsp
	// 정적 Static 메서드 선언!!!!!
	
	public static String saveFile(MultipartFile upfile, HttpSession session) {
		String originName = upfile.getOriginalFilename();
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); 
		int ranNum = (int)Math.random() * 90000 + 10000;
		String ext = originName.substring(originName.lastIndexOf("."));
		String changeName = currentTime + ranNum + ext; 
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
		try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return "resources/uploadFiles/" + changeName;
	}
	
	// 게시글 상세조회
	@GetMapping("detail.ch")
	public String selectChallengeDetail(int activityNo, /*int userNo,*/ Model model) {
		
		/*
		 * // 변수 HashMap<String, Integer> map = new HashMap(); map.put("userNo",
		 * userNo); map.put("challengeNo", challengeNo);
		 */
		
		// 1. 성공적으로 조회수 증가시
		if(challengeService.increaseViewCount(activityNo) > 0) {
			
			//System.out.println("내가 찍히면 조회수 증가" + challengeService.increaseViewCount(challengeNo));
			
			// 2. boardDetailView.jsp상 필요한 데이터를 조회 
			model.addAttribute("challenge", challengeService.selectChallengeDetail(activityNo));
			model.addAttribute("likeCount", challengeService.selectLikeCount(activityNo));
			
			model.addAttribute("userId",  challengeService.selectUserId(activityNo));
			model.addAttribute("categoryName",  challengeService.selectCategoryName(activityNo));
			
			//model.addAttribute("likedUser", challengeService.selectLikedUser(map));// detailView로 가야하기에 여기서 addAttribute
			
			System.out.println("내가 찍히면 challenge객체 넘어온 것" + challengeService.selectChallengeDetail(activityNo) );
			return "challenge/challengeDetailView";
			
		} else {
			
			System.out.println("조회수 증가 실패!");
			return "common/errorPage";
		}
	
	}
	
	
	@RequestMapping("updateForm.ch")
	public String updateForm(Challenge c, Model model) {
		
		// 이미존재하는 정보도 넘겨
		model.addAttribute("challenge", c);
		return "challenge/challengeUpdateForm";
	}
	
	
	
	
	
	@PostMapping("update.ch")
	public String updateChallenge(Challenge c, Activity a, MultipartFile upfile, HttpSession session, Model model) {
	
		System.out.println("나는 추상클래스 구현한 자식 : " + c);
		System.out.println("나는 추상클래스 부모 : " + a);
		HashMap<String, Challenge> map = new HashMap();
		map.put("challenge", c);

//		=======================파일 다시 공부하기==========================
		//	1. upfile이 새로 첨부된 경우 
		if( !upfile.getOriginalFilename().equals("") ) {
			
			 
			c.setOriginName(upfile.getOriginalFilename());
			c.setChangeName(saveFile(upfile, session));
		 
		// 2. upfile 첨부 안 한 경우 == 원래 있는 거 보여줘야
		} else {
			
			c.setOriginName(c.getOriginName());
			c.setChangeName(c.getChangeName());
		}
		// upfile을 삭제한 경우
//		=======================파일 다시 공부하기==========================


		System.out.println(c);
		if(challengeService.updateChallenge(map) > 0 ) {
			// update 성공, DB바뀜 = 새로운 리스트를 불러와야

			
			session.setAttribute("alertMsg", "게시글 수정 성공!");
			return "redirect:/challenge";
			//return "challenge/challengeListView";
			
		} else {
			
			return "common/errorPage";
		}
		
		
	}
	
	// 쿼리스트링으로 값을 전달하는 건 GET방식 => GETMapping으로 받아야함(POST가 아니라)
	@GetMapping("delete.ch")
	public String deleteChallenge(int activityNo, HttpSession session) {
		
		if(challengeService.deleteChallenge(activityNo) > 0) {
			
			session.setAttribute("alertMsg", "게시글 삭제 성공!");
			return "redirect:/challenge";
			
		} else {
		
			return "common/errorPage";
		}
		
	}
	
	


	
	
}