package edu.og.project.afterTradeReview.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.afterTradeReview.model.dto.ReviewWrite;
import edu.og.project.afterTradeReview.model.service.afterTradeReviewService;

@Controller
@RequestMapping("/review")
public class afterTradeReviewController {
	
	@Autowired
	private afterTradeReviewService service;
	
	@GetMapping("/joonggo")
	public String jonnggoReview() {
		
		return "afterTradeReview/used_review";
	}
	
	@GetMapping("/goods")
	public String goodsReview() {
		
		return "afterTradeReview/goods_review";
	}
	
	// 중고상품,굿즈 후기 등록
	@PostMapping("/write")
	public String writeReview(
	 	/*  @RequestParam int question1,
            @RequestParam int question2,
            @RequestParam int question3,
            @RequestParam int question4,
            @RequestParam String comment,                     
            @RequestParam("reviewImage") MultipartFile reviewImage, 
            @RequestParam String reviewType,
            @RequestParam int buyerNo,
            @RequestParam String BoardNo, */
			@ModelAttribute ReviewWrite reviewWrite,
            RedirectAttributes ra
          ) throws IllegalStateException, IOException 
	  { 
	        // Mapper나 Service로 전달하여 DB 저장 처리
	        // reviewService.saveReview(...);
		
		// DTO로 데이터 접근
	    // System.out.println(reviewWrite.getComment());
	    // System.out.println(reviewWrite.getReviewImage().getOriginalFilename());

		// 서비스 호출
	    int result = service.insertReview(reviewWrite);
		
		String path = null;
		
		if(result > 0) {
			// path = "redirect:/"+ result;
			path = "redirect:/review/goods";
			ra.addFlashAttribute("message", "후기가 등록되었습니다.");
		}else {
			path = "redirect:/review/goods";
			ra.addFlashAttribute("message", "후기 등록이 실패하였습니다.");
		}
		
		return path;
	}

}
