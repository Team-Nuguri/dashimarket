package edu.og.project.afterTradeReview.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	// 중고상품,굿즈 후기 등록
	@PostMapping("/write")
    public String reviewWrite(
    	   	    ReviewWrite reviewWrite,
	            RedirectAttributes ra) throws IllegalStateException, IOException 
	{

	        // Mapper나 Service로 전달하여 DB 저장 처리
	        // reviewService.saveReview(...);

        String result = service.reviewInsert(reviewWrite);
		
		String path = null;
		
		if(result != null) {
			path = "redirect:/"+ result;
			ra.addFlashAttribute("message", "상품이 등록되었습니다.");
		}else {
			path = "redirect:/goods/write";
			ra.addFlashAttribute("message", "상품 등록 실패");
		}
		
		return path;
	}

}
