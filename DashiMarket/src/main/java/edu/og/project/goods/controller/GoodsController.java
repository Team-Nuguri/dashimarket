package edu.og.project.goods.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Member;
import edu.og.project.common.dto.Review;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.dto.GoodsWrite;
import edu.og.project.goods.model.dto.OtherGoods;
import edu.og.project.goods.model.service.GoodsService;

@Controller
public class GoodsController {

	@Autowired
	private GoodsService service;

	// 굿즈 상품 목록 조회
	@GetMapping("/{boardType:g.*}")
	public String selectGoodsList(@PathVariable("boardType") String boardType,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		Map<String, Object> map = service.selectGoodsList(boardType, cp);
		System.out.println(map);

		model.addAttribute("map", map);
		return "goodsPage/goodsHome";
	}

	// 굿즈 정렬 (비동기)
	@GetMapping(value = "/{boardType:g.*}/sort", produces = "application/html; charset=UTF-8") /*
	 * json이 아닌 html 형태로
	 * 보내주기
	 */
	public String sortGoodsList(@PathVariable("boardType") String boardType,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
			@RequestParam("sortType") String sortType) {
		// 정렬 방식 넘겨주기
		Map<String, Object> resultMap = service.sortGoodsList(boardType, cp, sortType);
		model.addAllAttributes(resultMap); // return에 바로 resultMap을 담지 않고, model로 넘겨줌

		return "goodsPage/goodsHome :: #goods-container"; // 동적으로 바뀔 부분의 아이디
	}

	// 파비콘 요청 막기
	@GetMapping("favicon.ico")
	@ResponseBody // 뷰 리졸버를 거치지 않고 응답을 바로 보냄
	public void returnNoFavicon() {
		// 브라우저에게 아무것도 없다는 의미로 응답을 돌려줌
	}

	// 굿즈 상세 조회
	@GetMapping("/{boardType}/{boardNo:G.*}")
	public String selectGoodsDetail(@PathVariable("boardType") String boardType,
			@PathVariable("boardNo") String boardNo,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		Map<String, Object> map = new HashMap<>();

		Goods goods = service.selectGoodsDetail(boardNo);
		
		List<OtherGoods> goodsList = service.selectOtherGoodsList(boardNo);

		model.addAttribute("goods", goods);
		model.addAttribute("goodsList", goodsList);
		

		return "goodsPage/goodsDetail";
	}

	// 리뷰 목록 조회
	@GetMapping(value = "/{boardType}/review", produces="application/html; charset=UTF-8") 
	public String selectReviewList(@RequestParam String boardNo 
			,@RequestParam(value="cp",required = false, defaultValue = "1") int cp 
			,@RequestParam(value="sort", required = false, defaultValue = "basic") String sort
			, @PathVariable("boardType")String boardType 
			,Model model) {

		Map<String, Object> map = service.selectReviewList(boardNo, cp, sort); 
		Goods goods = service.selectGoodsDetail(boardNo);


		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("reviewList", map.get("review"));
		model.addAttribute("goods", goods);

		return "/goodsPage/goodsDetail :: #review-list";

	}


	// qna 목록 조회

	@GetMapping(value = "/{boardType}/qna", produces="application/html; charset=UTF-8") 
	public String selectQnaList(@RequestParam String boardNo 
			,@RequestParam(value="cp",required = false, defaultValue = "1") int cp 
			, @PathVariable("boardType") String boardType 
			,Model model) {

		Map<String, Object> map = service.selectQnaList(boardNo, cp); Goods goods =
				service.selectGoodsDetail(boardNo);

		System.out.println(map.get("comment"));

		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("qnaList", map.get("comment"));
		model.addAttribute("goods", goods);

		return "/goodsPage/goodsDetail :: #qna-list";

	}
	
	
	
	// 굿즈 상품 삭제
	@GetMapping("/goods/delete")
	public String goodsDelete(String boardNo
			, RedirectAttributes ra) {
		
		
		int result = service.goodsDelete(boardNo);
		
		String path = null;
		
		if(result != 0) {
			ra.addFlashAttribute("message", boardNo + "번 상품이 삭제되었습니다.");
			path = "redirect:/goods";
		}else {
			
			ra.addFlashAttribute("message", "삭제 실패하였습니다.");
			path = "redirect:/goods/"+boardNo;
			
		}
				
		return path;	
	}
	
	// 굿즈 상품 등록 페이지 전환
	@GetMapping("/goods/goodsWrite")
	public String goodsWriteForward() throws IllegalStateException, IOException {
		
		return "/goodsPage/goodsWrite";
	}
	
	
	
	// 굿즈 상품 등록
	@PostMapping("/{boardType:g.*}/write")
	public String goodsWrite(GoodsWrite goodsWrite
			, @PathVariable("boardType") String boardType
			, RedirectAttributes ra
			, @SessionAttribute("loginMember") Member loginMember
			) throws IllegalStateException, IOException {
		
		goodsWrite.setBoardType(boardType);
		goodsWrite.setMemberNo(loginMember.getMemberNo());
		
		String result = service.goodsInsert(goodsWrite);
		
		String path = null;
		
		if(result != "fali") {
			path = "redirect:/"+ boardType +"/" + result;
			ra.addFlashAttribute("message", "상품이 등록되었습니다.");
		}else {
			path = "redirect:/goods/write";
			ra.addFlashAttribute("message", "상품 등록 실패");
		}
		
		return path;
		
	}
	
	// 굿즈 수정 화면 전환
	@GetMapping("/goods/update")
	public String goodsUpdateForward(String boardNo
			, Model model) {
		
		
		Goods goods = service.selectGoodsDetail(boardNo);
		
		model.addAttribute("goods", goods);
		
		
		return "/goodsPage/goodsUpdate";
		
	}
	
	// 굿즈 수정
	@PostMapping("/{boardType:g.*}/update")
	public String goodsUpdate(
			@PathVariable("boardType") String boardType,
			@ModelAttribute GoodsWrite goods,
			RedirectAttributes ra
			) throws IllegalStateException, IOException {
		
		System.out.println(goods.getGoodsInfo().getSize());
		System.out.println(goods.getGoodsImg().getSize());
		
		goods.setBoardType(boardType);
		
		int result = service.goodsUpdate(goods);
		
		String path= null;
		
		if(result != 0) {
			ra.addFlashAttribute("message", "수정되었습니다.");
			path = "redirect:/goods/"+goods.getGoodsNo();
			
		}else {
			ra.addFlashAttribute("message", "수정 실패하였습니다.");
			path = "redirect:/goods/"+goods.getGoodsNo();
		}
		
		
		return path;
		
	}
	


	

}
