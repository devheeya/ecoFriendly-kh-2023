package com.kh.eco.event.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.kh.eco.challenge.controller.ChallengeController;
import com.kh.eco.event.model.service.EventService;
import com.kh.eco.event.model.vo.Event;

import lombok.RequiredArgsConstructor;

   
@RequiredArgsConstructor
@Controller 
public class EventController {

	/*
	 * @ResponseBody => "event/eventListView" 자체가 화면에 나옴
	 * 
	 * 
	 */
	
	private final EventService eventService;

	// 이벤트 전체조회
	@GetMapping("event")
    public String selectEventList(Model model) {

		model.addAttribute("list", eventService.selectEventList());
		System.out.println(eventService.selectEventList());
		
        return "event/eventListView";
    }
	  

	/*
	 * // 이벤트 세부모달
	 * 
	 * @ResponseBody
	 * 
	 * @RequestMapping(value="detail.ev",
	 * produces="application/json; charset=UTF-8") public String
	 * selectEventDetail(int eventNo, Model model) {
	 * 
	 * System.out.println(eventNo); Event event =
	 * eventService.selectEventDetail(eventNo);
	 * 
	 * return new Gson().toJson(event); }
	 */
	
	// 이벤트 등록
	 @PostMapping("insert.ev") 
	 public String insertEvent(	MultipartFile upfile, 
											 Event e, 
											 Model model, 
											 HttpSession session) {
		  if( !upfile.getOriginalFilename().contentEquals("")) {
			  e.setOriginName(upfile.getOriginalFilename());
			  e.setChangeName(ChallengeController.saveFile(upfile, session));
		  }
		  if(eventService.insertEvent(e) > 0) {
			 session.setAttribute("alertMsg", "이벤트 등록 성공!");
			 model.addAttribute("e", e);
			 return "redirect:/event"; 
		 } else { return "common/errorPage"; }
	}
	  

	  
	 // 이벤트 수정
	 @ResponseBody
	  @PostMapping("update.ev") 
	  public String updateEvent(Event event) {
		  System.out.println(event);
		  
		  System.out.println(eventService.updateEvent(event));
		  if(eventService.updateEvent(event) >0) {
			  return "success";
		  } else {
			  return "fail";
		  }
	  }
	  
	  // 이벤트 삭제
	  @PostMapping("delete.ev") 
	  public String deleteEvent(int activityNo) {
		  return "event/eventDetailView"; 
	  }
	 
	    @GetMapping("join.ev")
	    public  String joinEvent(Integer userNo, Integer activityNo) {
	       	System.out.println(userNo);
	    	System.out.println(activityNo);
	    	
	    	HashMap<String, Integer> map = new HashMap();
	    	map.put("userNo", userNo);
	    	map.put("activityNo", activityNo);
	    	
	    	if(eventService.joinEvent(map)>0) {
	    		return "success";
	    	} else {
	    		return "fail";
	    	}
	    
	    }

	
}
