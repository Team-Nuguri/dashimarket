package edu.og.project.seller.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.common.dto.Member;
import edu.og.project.common.dto.Review; 
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.seller.model.dao.SellerMapper;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerMapper mapper;

    @Override
    public Member getSellerInfo(int memberNo) {
        return mapper.getSellerInfo(memberNo);
    }

    @Override
    public Double getAvgRating(int memberNo) {
        return mapper.getAvgRating(memberNo);
    }

    @Override
    public int getProductCount(int memberNo) {
        return mapper.getProductCount(memberNo);
    }

    @Override
    public int getReviewCount(int memberNo) {
        return mapper.getReviewCount(memberNo);
    }

    @Override
    public List<Joonggo> getSellerProducts(int memberNo) {
        return mapper.getSellerProducts(memberNo);
    }

    @Override
    public List<Review> getSellerReviews(int memberNo) {
        return mapper.getSellerReviews(memberNo);
    }
}
