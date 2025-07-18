package com.minhojpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.minhojpa.repository.MemberRepository;
import com.minhojpa.repository.PlaceRepository;
import com.minhojpa.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;
	private final PlaceRepository placeRepository;
	private MemberRepository memberRepository;
	
}
