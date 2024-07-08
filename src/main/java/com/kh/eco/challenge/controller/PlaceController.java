package com.kh.eco.challenge.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.event.model.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlaceController {

	public static final String SERVICEKEY = "60341B09-531E-3CD0-AB70-EAFA320EB716";
	
	@GetMapping(value="search.place", produces="application/json; charset=UTF-8")
	public String searchPlace(String place) throws IOException {
		
		String url = "https://api.vworld.kr/req/search";
		url += "?request=search";
		url += "&errorformat=json";
		url += "&type=place";
		url += "&query="+place;
		url += "&crs=EPSG:900913";
		url += "&key="+SERVICEKEY;

		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String responseText = br.readLine();

		br.close();
		urlConnection.disconnect();
		
		return responseText;		
	}

	@RequestMapping("map/{x}/{y}")//요청url
	public String searchPlaceMap(@PathVariable("x")	@ModelAttribute String x, //""는 url에 넣은 값이름
													@PathVariable("y")	@ModelAttribute String y) {
		
		System.out.println(x);
		System.out.println(y);
		
		return "challenge/searchPlaceMap";
		
	}
	
}//class
