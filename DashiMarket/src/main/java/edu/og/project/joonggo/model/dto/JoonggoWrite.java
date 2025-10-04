package edu.og.project.joonggo.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoonggoWrite {
	
	
	private String joonggoNo;
	private String joonggoTitle;
	private String joonggoContent;
	private String categoryId;
	private int joonggoPrice;
	private int memberNo;
	private String boardType;
	
	private List<MultipartFile> imageList;

}
