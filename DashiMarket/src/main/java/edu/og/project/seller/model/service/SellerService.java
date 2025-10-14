package edu.og.project.seller.model.service;

import java.util.List;

import edu.og.project.common.dto.Member;
import edu.og.project.common.dto.Review; 
import edu.og.project.joonggo.model.dto.Joonggo;

public interface SellerService {

    // 판매자 기본 정보 조회
    Member getSellerInfo(int memberNo);

    // 판매자 평균 별점 조회
    Double getAvgRating(int memberNo);

    // 판매자 판매 물품 개수
    int getProductCount(int memberNo);

    // 판매자 거래후기 개수
    int getReviewCount(int memberNo);

    // 판매자 판매 물품 목록
    List<Joonggo> getSellerProducts(int memberNo);

    // 판매자 거래후기 목록
    List<Review> getSellerReviews(int memberNo);
}
