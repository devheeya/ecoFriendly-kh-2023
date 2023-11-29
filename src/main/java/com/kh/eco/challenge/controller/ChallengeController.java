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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.challenge.model.service.ChallengeService;
import com.kh.eco.challenge.model.vo.Challenge;
import com.kh.eco.common.model.template.Pagination;
import com.kh.eco.common.model.vo.PageInfo;
import com.kh.eco.event.model.service.EventService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChallengeController {

	
	private final ChallengeService challengeService;
	
	// 전체 리스트 조회
	@RequestMapping("challenge")
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
	@RequestMapping("search.condition")
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
	@RequestMapping("search.status")
	public String selectChallengeStatus(@RequestParam(value="currentPage", defaultValue="1")int currentPage, Model model, String status) {
		
		
		  // 0. Map에 status 키와 밸류 담기
		HashMap<String, String> map = new HashMap(); 
		map.put("status", status);
		 
		
		
		// 1. pi가져오기
		PageInfo pi = Pagination.getPageInfo( 
				challengeService.countChallengeStatus(map),// 검색결과수 구하기
				currentPage,
				4,
				5
				);
		
	
		// 2. 페이징정보 불러오기 : model, modelAndView, session 셋 중 하나
		model.addAttribute("status", status);
		model.addAttribute("list", challengeService.selectChallengeStatus(map, pi));	// status가지고 DB갔다오기
		model.addAttribute("pi", pi);
		
		//System.out.println("정렬조회결과개수 : " + challengeService.countChallengeStatus(map));
		
		
		return "challenge/challengeListView";
	}

	
	
	
	
	// challengeEnrollForm으로 가는 매핑메서드
	@RequestMapping("enrollForm.ch")
	public String challengeEnrollForm() {
		
		return "challenge/challengeEnrollForm";
		
	}
	
	// insert완료시 목록뷰로 감
	@RequestMapping("insert.ch")
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
		
		// 원본파일명 뽑기(필드명과 같게)
		String originName = upfile.getOriginalFilename();
		
		// 현재시각 문자열 특정형식으로 담기
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 왜 sql이 아니지?
		
		// 랜덤값 얻기 (*범위 + 초기값)
		int ranNum = (int)Math.random() * 90000 + 10000;
		
		// 확장자 뽑기
		String ext = originName.substring(originName.lastIndexOf("."));
		
		// 새파일명 정의(필드명과 같게)
		String changeName = currentTime + ranNum + ext; // String끼리 더하면 그냥 이어붙인 결과
		
		// 업로드한 파일을 저장할 경로 설정
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
		
		// 업로드한 파일을 새 파일객체로 이동시킴 == 파일전송
		try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {
			
			e.printStackTrace();
		}
		
		// 파일전송완료시 그 파일이 있는 경로를 반환해줘야함
		return "resources/uploadFiles/" + changeName;
		
	}
	
	// 게시글 상세조회
	@RequestMapping("detail.ch")
	public String selectChallengeDetail(int challengeNo, /*int userNo,*/ Model model) {
		
		/*
		 * // 변수 HashMap<String, Integer> map = new HashMap(); map.put("userNo",
		 * userNo); map.put("challengeNo", challengeNo);
		 */
		
		// 1. 성공적으로 조회수 증가시
		if(challengeService.increaseViewCount(challengeNo) > 0) {
			
			//System.out.println("내가 찍히면 조회수 증가" + challengeService.increaseViewCount(challengeNo));
			
			// 2. boardDetailView.jsp상 필요한 데이터를 조회 
			model.addAttribute("challenge", challengeService.selectChallengeDetail(challengeNo));
			model.addAttribute("likeCount", challengeService.selectLikeCount(challengeNo));
			
			model.addAttribute("userId",  challengeService.selectUserId(challengeNo));
			model.addAttribute("categoryName",  challengeService.selectCategoryName(challengeNo));
			
			//model.addAttribute("likedUser", challengeService.selectLikedUser(map));// detailView로 가야하기에 여기서 addAttribute
			
			System.out.println("내가 찍히면 challenge객체 넘어온 것" + challengeService.selectChallengeDetail(challengeNo) );
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
	
	
	
	
	
	@RequestMapping("update.ch")
	public String updateChallenge(Challenge c, MultipartFile upfile, HttpSession session, Model model) {
	
		
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
		if(challengeService.updateChallenge(c) > 0) {
			// update 성공, DB바뀜 = 새로운 리스트를 불러와야

			
			session.setAttribute("alertMsg", "게시글 수정 성공!");
			return "redirect:/challenge";
			//return "challenge/challengeListView";
			
		} else {
			
			return "common/errorPage";
		}
		
		
	}
	
	@RequestMapping("delete.ch")
	public String deleteChallenge(int challengeNo, HttpSession session) {
		
		if(challengeService.deleteChallenge(challengeNo) > 0) {
			
			session.setAttribute("alertMsg", "게시글 삭제 성공!");
			return "redirect:/challenge";
			
		} else {
		
			return "common/errorPage";
		}
		
	}
	
	


	
	
}