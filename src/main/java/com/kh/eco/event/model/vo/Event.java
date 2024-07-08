package com.kh.eco.event.model.vo;

import java.sql.Date;

import com.kh.eco.common.model.vo.Activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class Event  extends Activity  {
	
	private int activityNo;
	private String eventTitle;
	private String eventContent;
	private String eventPlace;
	private String eventDate;
	private int participants;
	
	
	// private List<User> participantsList;
	
}
