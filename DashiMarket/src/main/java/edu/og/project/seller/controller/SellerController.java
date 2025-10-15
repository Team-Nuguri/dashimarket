package edu.og.project.seller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.og.project.common.dto.Member;
import edu.og.project.common.dto.Review; 
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.seller.model.service.SellerService;

@Controller
@RequestMapping("/seller")
@SessionAttributes("seller")
public class SellerController {

    @Autowired
    private SellerService service;

    /**
     * 판매자 정보 페이지
     */
    @GetMapping("/{memberNo}")
    public String sellerProfile(@PathVariable("memberNo") int memberNo, Model model) {
        
        // 판매자 기본 정보 조회
        Member seller = service.getSellerInfo(memberNo);
        
        if (seller == null) {
            return "redirect:/";
        }
        
        // 판매자 평균 별점 조회
        Double avgRating = service.getAvgRating(memberNo);
        
        // 판매자 판매 물품 개수
        int productCount = service.getProductCount(memberNo);
        
        // 판매자 거래후기 개수
        int reviewCount = service.getReviewCount(memberNo);
        
        model.addAttribute("seller", seller);
        model.addAttribute("avgRating", avgRating != null ? avgRating : 0.0);
        model.addAttribute("productCount", productCount);
        model.addAttribute("reviewCount", reviewCount);
        
        return "seller/seller-profile";
    }

    /**
     * 판매자 판매 물품 목록 조회 (비동기)
     */
    @GetMapping("/{memberNo}/products")
    @ResponseBody
    public List<Joonggo> getSellerProducts(@PathVariable("memberNo") int memberNo) {
        return service.getSellerProducts(memberNo);
    }

    /**
     * 판매자 거래후기 목록 조회 (비동기)
     */
    @GetMapping("/{memberNo}/reviews")
    @ResponseBody
    public List<Review> getSellerReviews(@PathVariable("memberNo") int memberNo) {
        return service.getSellerReviews(memberNo);
    }
}
