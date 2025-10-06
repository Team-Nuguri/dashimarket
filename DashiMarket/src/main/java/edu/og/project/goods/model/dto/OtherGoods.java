package edu.og.project.goods.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OtherGoods {
	/*<id property="goodsNo" column="BOARD_NO"/>
	 <result property="goodsTitle" column="BOARD_TITLE"/>
	 <result property="imagePath" column="IMG_PATH" />		   
	 <result property="imageRename" column="IMG_RENAME" />		   
	 <result property="goodsPrice" column="GOODS_PRICE"/>*/
	 
	  private String goodsNo;
	  private String goodsTitle;
	  private String imagePath;
	  private String imageRename;
	  private int goodsPrice;
	  
}
