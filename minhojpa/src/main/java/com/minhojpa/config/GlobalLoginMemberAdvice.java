package com.minhojpa.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.minhojpa.entity.Member;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalLoginMemberAdvice {
	@ModelAttribute("loginMember")
	public Member loginMember(HttpSession session) {
		return (Member) session.getAttribute("loginMember");
	}
}
