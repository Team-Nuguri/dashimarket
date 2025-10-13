package edu.og.project.joonggo.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Pagination;
import edu.og.project.common.utility.ImageResizer;
import edu.og.project.common.utility.Util;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.joonggo.model.dao.JoonggoMapper;
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import edu.og.project.joonggo.model.dto.SimilarItem;
import edu.og.project.joonggo.model.exception.ImageDeleteException;
import net.coobird.thumbnailator.resizers.Resizer;

@Service
@PropertySource("classpath:/config.properties")
public class JoonggoServiceImpl implements JoonggoService {

	@Autowired
	JoonggoMapper mapper;

	@Value("${my.joonggo.webpath}")
	private String webPath;

	@Value("${my.joonggo.location}")
	private String filePath;


	// 중고 상품 목록 조회 (KJK)
	@Override
	public Map<String, Object> selectJoonggoList(String boardType, int cp) {
		// 특정 게시판의 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getJoonggoListCount(boardType);

		Pagination pagination = new Pagination(cp, listCount, 16);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());

		List<Joonggo> boardList = mapper.selectJoonggoList(boardType, rowBounds);

		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);

		return map;
	}


	// 중고 상품 목록 정렬 (KJK)
	@Override
	public Map<String, Object> sortJoonggoList(String boardType, int cp, String sortType) {
		int listCount = mapper.getJoonggoListCount(boardType);

		Pagination pagination = new Pagination(cp, listCount, 16);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());

		// 정렬 타입에 맞게 조회해오기
		List<Joonggo> sortList = null;

		if(sortType != null) {

			// 인기순(조회수?)
			if(sortType.equals("popular")) sortList = mapper.sortJoonggoViews(boardType, rowBounds);

			// 낮은 가격순
			if(sortType.equals("lowPrice")) sortList = mapper.sortJoonggoLowPrice(boardType, rowBounds);

			// 높은 가격순
			if(sortType.equals("highPrice")) sortList = mapper.sortJoonggoHighPrice(boardType, rowBounds);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", sortList);

		return map;
	}



	// 중고상품 상세 조회
	@Override
	public Joonggo selectJoonggoDetail(String joonggoNo) {


		return mapper.selectJoonggoDetail(joonggoNo);
	}



	// 비슷한 상품 목록 조회
	@Override
	public List<SimilarItem> selectJonggoList(Map<String, Object> map) {
		return mapper.selectJonggoList(map);
	}



	// 중고 상품 삭제
	@Override
	public int deleteJoonggoItem(String joonggoNo) {

		return mapper.deleteJoonggoItem(joonggoNo);
	}



	// 중고 상품 등록
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String joonggoInsert(JoonggoWrite joonggoWrite) throws IllegalStateException, IOException {
		
		// xss 방지 처리
		joonggoWrite.setJoonggoTitle(Util.XSSHandling(joonggoWrite.getJoonggoTitle()));
		joonggoWrite.setJoonggoContent(Util.XSSHandling(joonggoWrite.getJoonggoContent()));
		
		
		
		String result = null;
		

		// 텍스트 먼저 삽입
		int num = mapper.joonggoInsert(joonggoWrite);
		
		// 반환 결과 0이면 메소드 종료 
		if(num == 0) {

			return result;
		}

		// 가격 삽입
		num = mapper.joonggoPriceInsert(joonggoWrite);
		if(num == 0)throw new RuntimeException();

		if(num != 0) {
			// 이미지 분류
			List<Image> uploadImage = new ArrayList<>();

			// 비어있는 이미지 없으니 조건문 없이 그냥 바로 하나씩 꺼내서 Image DTO에 값 세팅후
			// uploadImage에 추가
			for (int i = 0; i < joonggoWrite.getImageList().size(); i++) {

				Image img = new Image();

				// 웹 접근 경로
				img.setImagePath(webPath);

				String orginalFileName = joonggoWrite.getImageList().get(i).getOriginalFilename();
				String ext = orginalFileName.substring(orginalFileName.lastIndexOf("."));

				img.setImageRename(joonggoWrite.getJoonggoNo()+(i+1)+ ext);

				img.setImageOrder(i);

				img.setBoardNo(joonggoWrite.getJoonggoNo());


				uploadImage.add(img);	
			}	


			// 실제 서벙 ㅔ 저장
			if(!uploadImage.isEmpty()){

				num = mapper.insertImage(uploadImage);

				// uploadImage 사이즈와 성공한 행의 개수가 같을 때
				if(uploadImage.size() == num){
			
					for (int i = 0; i < uploadImage.size(); i++) {

						String rename = uploadImage.get(i).getImageRename();
						
						
						// 이미지 리사이징 500 500 으로 리사이징 후 서버에 저장
						ImageResizer.resizeAndSave500x500(
								joonggoWrite.getImageList().get(i), 
								filePath, 
								rename
						);
					}
					result = joonggoWrite.getJoonggoNo();

				}else {

					throw new FileUploadException();
				}
			}
		}



		return result;
	}


	
	// 중고 상품 수정
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String joonggoUpdate(Map<String, Object> map) throws IllegalStateException, IOException {
		
		

		JoonggoWrite joonggoWrite = (JoonggoWrite) map.get("joonggoWrite");
		
		String updateResult =null;

		String deleteList = (String) map.get("deleteList");

		joonggoWrite.setJoonggoTitle(Util.XSSHandling(joonggoWrite.getJoonggoTitle()));
		joonggoWrite.setJoonggoContent(Util.XSSHandling(joonggoWrite.getJoonggoContent()));


		int result1 = mapper.joonggoUpdate(joonggoWrite);
		
		if(result1 == 0) return updateResult;

		int result2 = mapper.joonggoPriceUpdate(joonggoWrite);


		if(result2==0) {

			throw new RuntimeException(); 
		}
		
		updateResult = joonggoWrite.getJoonggoNo();

		// 이미지 삭제
		if(deleteList != null && !deleteList.equals("")) {

			Map<String, Object> deleteMap = new HashMap<>();
			deleteMap.put("joonggoNo", joonggoWrite.getJoonggoNo());
			deleteMap.put("deleteList", deleteList);

			int result = mapper.imageDelete(deleteMap);

			if(result == 0) {
				
				throw new ImageDeleteException();
			}

			// 이미지 정렬
			// 이미지 order 재정렬
			result = mapper.sortImageOrder(joonggoWrite.getJoonggoNo());
		}

		List<Image> uploadImage = new ArrayList<>();

		if(joonggoWrite.getImageList() != null) {
			
			// 해당 게시글 가장 큰 이미지 order 번호 조회
			int startOrder = mapper.selectImageOrder(joonggoWrite.getJoonggoNo()) + 1;


			for (int i = 0; i < joonggoWrite.getImageList().size(); i++) {

				Image img = new Image();

				// 웹 접근 경로
				img.setImagePath(webPath);

				String orginalFileName = joonggoWrite.getImageList().get(i).getOriginalFilename();
				
				// 확장자 추출
				String ext = orginalFileName.substring(orginalFileName.lastIndexOf("."));

				img.setImageRename(joonggoWrite.getJoonggoNo()+(i+startOrder)+ ext);

				img.setImageOrder(i+startOrder);

				img.setBoardNo(joonggoWrite.getJoonggoNo());


				uploadImage.add(img);	
			}	


			// 실제 서벙 ㅔ 저장
			if(!uploadImage.isEmpty()){

				result1 = mapper.insertImage(uploadImage);

				// uploadImage 사이즈와 성공한 행의 개수가 같을 때
				if(uploadImage.size() == result1){

					for (int i = 0; i < uploadImage.size(); i++) {

						String rename = uploadImage.get(i).getImageRename();
						
						
						// 이미지 리사이징 500 500 으로 리사이징 후 서버에 저장
						ImageResizer.resizeAndSave500x500(
								joonggoWrite.getImageList().get(i), 
								filePath, 
								rename
						);
					}
					

				}else {

					throw new FileUploadException();
				}
			}



		}

		return updateResult;
	}

	
	// 중고 삽입 or 삭제
	@Override
	public int joonggoLike(Map<String, Object> paramMap) {
		
		int result = 0;
		
		if(paramMap.get("likeCheck") == (Integer)1) {
			
			result = mapper.likeInsert(paramMap);
		} else {
			
			result = mapper.deleteInsert(paramMap);
		}
		
		// insert or delete 실패시 임의 숫자 반환
		if(result == 0) {
			return -1;
		}
		
		result = mapper.countLike(paramMap.get("joonggoNo"));
		
		
		return result;
	}


	
	// 좋아요 확인
	@Override
	public int likeSelect(Map<String, Object> map) {
		
		return mapper.likeSelect(map);
	}

	// 중고 상품 나의 위시리스트 목록 조회 (KJK)
	@Override
	public List<Joonggo> selectJoonggoWishList(int memberNo) {
		return mapper.selectJonggoWishList(memberNo);
	}

	

	 


	@Override
	public int updateReadCount(String joonggoNo) {
		
		return mapper.updateReadCount(joonggoNo);
	}

	// 중고 상품 카테고리 목록 조회 (KJK)
	@Override
	public Map<String, Object> selectJoonggoCategoryList(String categoryId, int cp) {
		 int listCount = mapper.getJoonggoCategoryListCount(categoryId);
		 
		  Pagination pagination = new Pagination(cp, listCount, 16); int offset =
		 (pagination.getCurrentPage() - 1) * pagination.getLimit(); RowBounds
		  rowBounds = new RowBounds(offset, pagination.getLimit());
		  
		  List<Joonggo> boardList = mapper.selectJoonggoCategoryList(categoryId, rowBounds);
		  
		  Map<String, Object> map = new HashMap<>(); map.put("pagination", pagination);
		  map.put("boardList", boardList);
		  
		  return map;
	}

  

}
