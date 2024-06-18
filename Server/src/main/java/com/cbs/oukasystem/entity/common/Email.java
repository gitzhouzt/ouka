package com.cbs.oukasystem.entity.common;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

	private String subject;

	private String from;

	private String to;

	private String[] cc;

	private String[] bcc;

	private List<MultipartFile> multipartFiles;

	private Date sendDate;

	private String text;

	private String status;

	private String errorMsg;

}