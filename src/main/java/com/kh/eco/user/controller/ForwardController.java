package com.kh.eco.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

	
	@RequestMapping("login")
	public String naverPage() {
		return "user/userLogin";	
	}
}
