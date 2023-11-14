package com.kh.eco.product.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @ToString
public class Product {
	private int productNo;
	private String brandName;
	private String productName;
	private String productInfo;
	private String detailInfo;
	private String extraInfo;
	private String category;
	private String mainImg;
	private int count;
	private String createDate;
}
