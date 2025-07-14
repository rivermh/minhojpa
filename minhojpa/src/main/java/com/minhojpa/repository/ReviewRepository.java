package com.minhojpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Place;
import com.minhojpa.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	//특정 장소(place) 에 등록된 리뷰 전체 조회
	List<Review> findBPlace(Place place);
}
