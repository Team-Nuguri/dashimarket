package edu.og.project.goods.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsWrite {
	
	private String goodsNo;
	private String goodsTitle;
	private String goodsContent; // 내용 이미지 경로 넣을 예정
	private MultipartFile goodsInfo;
	private MultipartFile goodsImg;
	private int goodsPrice;
	private int goodsStock;
	
	private int memberNo;
	private String boardType;
}
