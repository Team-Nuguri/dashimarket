package edu.og.project.community.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Pagination;
import edu.og.project.common.utility.Util;
import edu.og.project.community.model.dao.CommunityMapper;
import edu.og.project.community.model.dto.Community;

@Service
public class CommunityServiceImpl implements CommunityService {
	
	@Autowired
	private CommunityMapper mapper;
	
	@Value("${my.community.webpath}")
	private String webPath;

	@Value("${my.community.location}")
	private String filePath;
	
	// 커뮤니티 목록 조회
	@Override
	public Map<String, Object> selectCommunityList(String boardType, int cp, String category, String sort) {
		// 특정 게시판의 특정 카테고리에서 삭제되지 않은 게시글 수 조회
		Map<String, Object> countParam = new HashMap<>();
		countParam.put("boardType", boardType);
		countParam.put("category", category);
		
		int listCount = mapper.getListCount(countParam);
		
		// 페이지네이션
		Pagination pagination = new Pagination(cp, listCount);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		// 매퍼로 보낼 파라미터
		Map<String, Object> param = new HashMap<>();
		param.put("boardType", boardType);
		param.put("category", category);
		param.put("sort", sort);
		
		List<Community> boardList = mapper.selectCommunityList(param, rowBounds);
		
		// 조회 결과 return
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 커뮤니티 상세조회
	@Override
	public Community communityDetail(Map<String, Object> map) {
		return mapper.communityDetail(map);
	}

	// 댓글 조회
	@Override
	public List<Comment> selectComment(Map<String, Object> map) {
		return mapper.selectCommentList(map);
	}

	// 댓글 등록
	@Override
	public int insertComment(Comment comment) {
		// XSS 방지 처리
		comment.setCommentContent(Util.XSSHandling(comment.getCommentContent()));
		
		int result = mapper.insertComment(comment);
		
		// 댓글 등록 성공시 댓글 번호 반환
		if(result > 0) {
			result = comment.getCommentNo();
		}
		
		return result;
	}

	// 커뮤니티 글쓰기
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String communityWrite(Community community, List<MultipartFile> images) throws IllegalStateException, IOException {
		// XSS 방지
		community.setBoardTitle(Util.XSSHandling(community.getBoardTitle()));
		community.setBoardContent(Util.XSSHandling(community.getBoardContent()));
		
		
		// 게시판 테이블에 이미지 제외 텍스트 삽입
		int result = mapper.communityWrite(community);
		
		// 삽입 실패시 종료
		if(result == 0) return null;
		
		// 시퀀스로 생성된 게시글 번호 가져오기
		String communityNo = community.getBoardNo();
		
		// 게시글 삽입 성공시 업로드된 이미지가 있다면 이미지 테이블에 삽입
		if(communityNo != null) {
			
			// 실제로 업로드된 이미지 담을 리스트
			List<Image> uploadList = new ArrayList<>();
			
			// 멀티파트 images에 담긴 파일 중 실제로 업로드된 파일만 분류하기
			for(int i = 0; i < images.size(); i++) {
				
				if(images.get(i).getSize() > 0) {
					Image img = new Image();
					img.setImagePath(webPath); // 웹 접근 경로
					
					String fileName = images.get(i).getOriginalFilename(); // 원본명
					String ext = fileName.substring(fileName.lastIndexOf("."));

					img.setImageRename(community.getBoardNo()+(i+1)+ ext);

					img.setImageOrder(i);

					img.setBoardNo(community.getBoardNo());


					uploadList.add(img);	
				}
			} // end of for
			
			// 분류 작업 후에 리스트가 비어있지 않은 경우(업로드한 이미지가 하나라도 존재할 때)
			if(!uploadList.isEmpty()) {
				int imgResult = mapper.insertImageList(uploadList);
				
				// 삽입된 행의 개수 = uploadList 개수라면 전체 insert 성공
				// 서버에 파일 저장하기
				if(imgResult == uploadList.size()) {
					
					for(int i = 0; i < uploadList.size(); i++) {
						int index = uploadList.get(i).getImageOrder();
						
						String rename = uploadList.get(i).getImageRename();
						
						images.get(index).transferTo(new File(filePath + rename));
					}
				} else {
					 // 강제 예외 발생
					throw new FileUploadException();
				}
					
			}
		}
		
		// 게시글 번호
		return communityNo;
	}


}
