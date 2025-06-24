package com.minhojpa.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minhojpa.service.MemberService;

@RestController
public class MemberRestController {
	private final MemberService memberService;
	
	@Autowired
	public MemberRestController(MemberService memberService) {
		this.memberService = memberService;
	}
	
    @GetMapping("/members/check-email")
    public Map<String, Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean exists = memberService.existsByEmail(email);
        return Collections.singletonMap("exists", exists);
    }
}
