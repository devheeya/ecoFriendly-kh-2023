package com.kh.eco.common.model.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString

public abstract class Activity   {

	private int activityNo;
	private String status;
	private String originName;
	private String changeName;
	private Date uploadDate;
	private Date modifyDate;
	private int categoryNo;
	private int type;
	
}
