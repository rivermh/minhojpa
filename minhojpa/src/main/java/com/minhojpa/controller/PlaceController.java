package com.minhojpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.minhojpa.entity.Place;
import com.minhojpa.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {
	private final PlaceRepository placeRepository;
	
	//장소 목록 조회 페이지
	@GetMapping
	public String list(Model model) {
		List<Place> places = placeRepository.findAll();
		model.addAttribute("places", places);
		return "places/list";
	}
	
	// 장소 등록 폼 페이지
	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("place", new Place());
		return "places/createForm";
	}
	
	
	// 장소 등록 처리
	@PostMapping("/new")
	public String create(Place place) {
		placeRepository.save(place);
		return "redirect:/places";
	}
	
	// 장소 상세 페이지
	@GetMapping("/{id}")
	public String detail(@PathVariable Long id, Model model) {
		Place place = placeRepository.findById(id).orElse(null);
		if(place == null){
			return "redirect:/places"; //혹은 오류 페이지로 리다이렉트
		}
		model.addAttribute("place", place);
		model.addAttribute("posts", place.getPosts());
		return "places/detail";
	}
}
