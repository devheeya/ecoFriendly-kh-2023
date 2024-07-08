package com.kh.eco.challenge.model.vo;




import java.sql.Date;
import java.util.List;

import com.kh.eco.achievement.model.vo.Achievement;
import com.kh.eco.common.model.vo.Activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class Challenge extends Activity {

	private int activityNo;
	private String challengeTitle;
	private String challengeContent;
	private String challengePlace;
	
	private int achievementCount;
	private int successLimit;
	private int minParticipant;
	private int maxParticipant;
	
	private int viewCount;
	private int likeCount;
	private String startDate;
	private String endDate;
	private int userNo;
	
	//private List<Achievement> achieveList;

}
