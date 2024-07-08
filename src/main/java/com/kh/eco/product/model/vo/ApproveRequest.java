
package com.kh.eco.product.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString
public class ApproveRequest {
	private String cid;
	private String tid;
	private String partnerOrderId;
	private int userNo;
	private String pgToken;
}


