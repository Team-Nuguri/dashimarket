package edu.og.project.afterTradeReview.model.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.afterTradeReview.model.dao.afterTradeReviewMapper;
import edu.og.project.afterTradeReview.model.dto.ReviewWrite;

@Service
public class afterTradeReviewServiceImpl implements afterTradeReviewService {
	
	@Autowired
	private afterTradeReviewMapper mapper;

    // 중고상품, 굿즈 후기/별점 등록
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertReview(ReviewWrite reviewWrite)  
	{
		
		/*
		 * // xss 방지 처리 (추후 추가 요망)
		 * 
		 */
		
		 // 1️.업로드 파일 확인
        if (!reviewWrite.getReviewImage().isEmpty()) {
            try {
                // 2️.업로드 폴더 지정
                String filePath = "C:/dashimarketImg/afterTradeReview/"; // 실제 서버 경로
                File directory = new File(filePath);
                if (!directory.exists()) directory.mkdirs();

                // 3️. 원본 파일명과 확장자 추출
                String originalName = reviewWrite.getReviewImage().getOriginalFilename();
                String ext = originalName.substring(originalName.lastIndexOf("."));

                // 4️. 파일명 중복 방지 (UUID + 날짜)
                String rename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                        + "_" + UUID.randomUUID().toString().substring(0, 6) + ext;

                // 5️. 파일 저장
                File targetFile = new File(filePath + rename);
                reviewWrite.getReviewImage().transferTo(targetFile);

                // 6️. DTO에 파일 정보 저장
                reviewWrite.setReviewFilePath("/dashimarketImg/afterTradeReview/");
                reviewWrite.setReviewFileName(rename);

            } catch (IOException e) {
                e.printStackTrace();
                return 0; // 업로드 실패 시 등록 중단
            }
        }

        // 7️. DB Insert
         // 중고상품, 굿즈 후기 먼저 insert 
         int result = mapper.reviewInsert(reviewWrite);

		 if(result == 0) { 
			 return result; 
		  }
		
		 // 중고상품, 굿즈 후기 별점 insert 
		 result = mapper.reviewRatingInsert(reviewWrite);
		 
		 if(result == 0) { 
			 return result;
		 }
		 
		 // 중고상품 후기작성여부 update (굿즈는 update 하지않음)
		 result = mapper.reviewFlagUpdate(reviewWrite);
		 
		 if(result == 0) { 
			 return result+1;
		 }
		 
		 return result;
		
	}

	@Override
	public Map<String, Object> getJoonggoReviewInfo(String boardNo, int memberNo) {
		  Map<String, Object> params = new HashMap<>();
		  params.put("boardNo", boardNo);
		  params.put("memberNo", memberNo);
		  return mapper.selectJoonggoReviewInfoMap(params);
	}

	@Override
	public Map<String, Object> getGoodsReviewInfo(String boardNo, int memberNo) {
		  Map<String, Object> params = new HashMap<>();
		  params.put("boardNo", boardNo);
		  params.put("memberNo", memberNo);
		  return mapper.selectGoodsReviewInfoMap(params);
		
	}


}
