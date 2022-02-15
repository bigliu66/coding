package com.lx.log.logrecord.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
	private String logId;
	private String bizNo;
	private String operator;
	private String category;
	private String content;
	private String exception;
	private Boolean success;
	private String returnStr;
	private Date operateDate;
}