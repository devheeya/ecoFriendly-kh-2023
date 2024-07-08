package com.kh.eco.challenge.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.challenge.model.service.ChallengeService;
import com.kh.eco.event.model.service.EventService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController 
public class AjaxChallengeController {

	
	private final ChallengeService challengeService;
	
	

	// Model을 쓸거면 ajax를 쓰면 안돼 Java랑 JS섞어쓰지마// produce="json"안해야 success가 돌아감
	@ResponseBody
	@GetMapping("checkLike.ch") 
	public String checkLikeCount(int userNo, 
												int activityNo, 
												Model model) {
		Map<String, Integer> map = new HashMap();

		map.put("userNo", userNo);
		map.put("activityNo", activityNo);
		
		if(challengeService.checkLikeCount(map) > 0) { 
			return "success";
		} else {
			return "fail";
		}
	}
	
	
	@PostMapping("insertLike.ch")
	public String insertLike(int userNo, int activityNo) {
		
		// 복합키인 userNo와 challengeNo를 map에 각각 담음
		HashMap<String, Integer> map = new HashMap();
		map.put("userNo", userNo);
		map.put("activityNo", activityNo);
		
		
		
		if(challengeService.insertLike(map)  > 0 ) {
			// 좋아요도 증가하고 좋아요테이블에도 한행추가
			
			return "success";
			
		} else {
			
			// 좋아요 증가 안하거나 테이블에 한행 추가X
			return "fail";
			
		}
		
	}	

	
	
	@PostMapping("deleteLike.ch")
	public String deleteLike(int userNo, int activityNo) {
		
		HashMap<String, Integer> map = new HashMap();
		map.put("userNo", Integer.valueOf(userNo));
		map.put("activityNo", Integer.valueOf(activityNo));
		
		if(challengeService.deleteLike(map)  > 0 ) {
		
			return "success";
			
		} else {
			
			return "fail";
			
		}
		
		
	}
	
	
}//class
