package edu.og.project.goods.model.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Pagination;
import edu.og.project.common.dto.Review;
import edu.og.project.common.utility.ImageResizer;
import edu.og.project.common.utility.Util;
import edu.og.project.goods.model.dao.GoodsMapper;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.dto.GoodsWrite;
import edu.og.project.goods.model.dto.OtherGoods;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper mapper;

	// info 웹 패스
	@Value("${my.goodsInfo.webpath}")
	private String iWebPath;

	// info 파일 패스
	@Value("${my.goodsInfo.location}")
	private String iFilePath;

	@Value("${my.goods.webpath}")
	private String webPath;

	@Value("${my.goods.location}")
	private String filePath;

	// 굿즈 상품 목록 조회
	@Override
	public Map<String, Object> selectGoodsList(String boardType, int cp) {
		// 특정 게시판의 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getListCount(boardType);

		Pagination pagination = new Pagination(cp, listCount, 16);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());

		List<Goods> boardList = mapper.selectGoodsList(boardType, rowBounds);

		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);

		return map;
	}

	// 굿즈 정렬 목록 조회
	@Override
	public Map<String, Object> sortGoodsList(String boardType, int cp, String sortType) {
		int listCount = mapper.getListCount(boardType);

		Pagination pagination = new Pagination(cp, listCount, 16);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());

		// 정렬 타입에 맞게 조회해오기
		List<Goods> sortList = null;

		if(sortType != null) {

			// 인기순(기본)
			if(sortType.equals("popular")) sortList = mapper.selectGoodsList(boardType, rowBounds);

			// 낮은 가격순
			if(sortType.equals("lowPrice")) sortList = mapper.sortLowPrice(boardType, rowBounds);

			// 높은 가격순
			if(sortType.equals("highPrice")) sortList = mapper.sortHighPrice(boardType, rowBounds);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", sortList);

		return map;
	}


	// 굿즈 상세 조회
	@Override
	public Goods selectGoodsDetail(String boardNo) {


		return mapper.selectGoodsDetail(boardNo);
	}


	// 굿즈 리뷰 목록 조회
	@Override
	public Map<String, Object> selectReviewList(String boardNo, int cp, String sort) {

		int listCount = mapper.getReviewListCount(boardNo);


		Pagination pagination = new Pagination(cp, listCount,5);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("boardNo", boardNo);
		paramMap.put("sort", sort);
		

		List<Review> review = mapper.selectReviewList(paramMap, rowBounds);

		Map<String, Object> map = new HashMap<>();

		map.put("pagination", pagination);
		map.put("review", review);

		return map;
	}



	// 굿즈 qna 목록 조회
	@Override
	public Map<String, Object> selectQnaList(String boardNo, int cp) {
		int listCount = mapper.getQnaListCount(boardNo);

		Pagination pagination = new Pagination(cp, listCount);
		int offset = (pagination.getCurrentPage()-1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());

		List<Comment> comment = mapper.selectCommentList(boardNo, rowBounds);

		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("comment", comment);

		return map;
	}


	// 다른 굿즈 리스트
	@Override
	public List<OtherGoods> selectOtherGoodsList(String boardNo) {

		return mapper.selectOtherGoodsList(boardNo);
	}


	// 굿즈 삭제
	@Override
	public int goodsDelete(String boardNo) {

		return mapper.goodsDelete(boardNo);
	}


	// 굿즈 상품 등록
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String goodsInsert(GoodsWrite goodsWrite) throws IllegalStateException, IOException {

		// xss 방지 처리
		goodsWrite.setGoodsTitle(Util.XSSHandling(goodsWrite.getGoodsTitle()));

		// goodsInfo 이미지



		goodsWrite.setGoodsContent(iWebPath+goodsWrite.getGoodsInfo().getOriginalFilename());

		// 보드 먼저 insert
		int result = mapper.goodsBoardInsert(goodsWrite);

		if(result == 0) {
			return "fail";
		}

		// 굿즈 가격 재고 insert
		result = mapper.goodsInsert(goodsWrite);

		String boardNo=goodsWrite.getGoodsNo();
		if(result == 0) {
			throw new IllegalStateException();
		}else {


			Image image = new Image();

			image.setImagePath(webPath);
			image.setBoardNo(boardNo);
			image.setImageOrder(0);
			image.setImageRename(Util.fileRename(goodsWrite.getGoodsImg().getOriginalFilename()));


			result = mapper.imageInsert(image);



			if(result != 0) {

				// 파일 리사이징 후 등록
				ImageResizer.resizeAndSave500x500(
						goodsWrite.getGoodsImg(), 
						filePath, 
						image.getImageRename()
						);
			}else {
				throw new FileUploadException();
			}
		}



		return goodsWrite.getGoodsNo();
	}



	// 굿즈 수정
	@Override
	@Transactional(rollbackFor = Exception.class) // 에러 발생 시 전체 롤백
	public int goodsUpdate(GoodsWrite goods) throws IllegalStateException, IOException {

		goods.setGoodsTitle(Util.XSSHandling(goods.getGoodsTitle()));

		// 상품 설명 이미지 변경사항 있을 경우
		if(goods.getGoodsInfo().getSize() > 0) {
			goods.setGoodsContent(iWebPath+goods.getGoodsInfo().getOriginalFilename());

		}

		int result = mapper.goodsBoardUpdate(goods);

		if(result == 0) {
			return 0;
		}

		// 굿즈 재고 수량 업데이트
		result = mapper.goodsUpdate(goods);


		if(result == 0) throw new RuntimeException();


		if(goods.getGoodsImg().getSize()>0) {

			Image image = new Image();

			image.setImagePath(webPath);
			image.setBoardNo(goods.getGoodsNo());
			image.setImageOrder(0);
			image.setImageRename(Util.fileRename(goods.getGoodsImg().getOriginalFilename()));

			 result = mapper.goodsImageUpdate(image);

			if(result !=0) {
				// 파일 리사이징 후 등록
				ImageResizer.resizeAndSave500x500(
						goods.getGoodsImg(), 
						filePath, 
						image.getImageRename()
						);
			}else {
				throw new FileUploadException();
			}

		}



		return result;
	}

	// 굿즈 페이지에서 헤더 검색
	@Override
	public List<Goods> goodsSearch(String query) {
		return mapper.goodsSearch(query);
	}






}
