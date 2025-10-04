package edu.og.project.joonggo.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.common.dto.Image;
import edu.og.project.common.utility.Util;
import edu.og.project.joonggo.model.dao.JoonggoMapper;
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import edu.og.project.joonggo.model.dto.SimilarItem;

@Service
@PropertySource("classpath:/config.properties")
public class JoonggoServiceImpl implements JoonggoService {
	
	@Autowired
	JoonggoMapper mapper;
	
	@Value("${my.joonggo.webpath}")
	private String webPath;
	
	@Value("${my.joonggo.location}")
	private String filePath;
	
	
	
	
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


	
	// 중고 상품 삽입
	@Override
	@Transactional
	public String joonggoInsert(JoonggoWrite joonggoWrite) throws IllegalStateException, IOException {
		
		joonggoWrite.setJoonggoTitle(Util.XSSHandling(joonggoWrite.getJoonggoTitle()));
		joonggoWrite.setJoonggoContent(Util.XSSHandling(joonggoWrite.getJoonggoContent()));
		
		String result = "false";
		
		// 텍스트 먼저 삽입
		int num = mapper.joonggoInsert(joonggoWrite);
		
		if(num == 0) {
			
			return result;
		}
		
		// 가격 삽입
		num = mapper.joonggoPriceInsert(joonggoWrite);
		if(num == 0)throw new RuntimeException();
		
		if(num != 0) {
			// 이미지 분류
			List<Image> uploadImage = new ArrayList<>();
			
			
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
						
						joonggoWrite.getImageList().get(i).transferTo(new File(filePath+rename));
						
					}
					result = joonggoWrite.getJoonggoNo();
					
				}else {
					
					throw new FileUploadException();
				}
			}
		}
		
		
		
		return result;
	}

}
