package com.minhojpa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minhojpa.entity.Member;
import com.minhojpa.entity.Place;
import com.minhojpa.entity.Review;
import com.minhojpa.repository.PlaceRepository;
import com.minhojpa.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    // 리뷰 저장
    public Review saveReview(String title, String content, Long placeId, Member writer) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("장소를 찾을 수 없습니다: id=" + placeId));
        
        Review review = new Review(title, content, place, writer);
        return reviewRepository.save(review);
    }

    // 특정 장소에 대한 리뷰 목록
    @Transactional(readOnly = true)
    public List<Review> getReviewsByPlace(Place place) {
        return reviewRepository.findByPlace(place);
    }
}
