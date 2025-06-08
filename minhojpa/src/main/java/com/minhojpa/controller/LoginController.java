package com.minhojpa.controller;

import com.minhojpa.entity.Member;
import com.minhojpa.service.LoginService;
import com.minhojpa.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @Autowired
    public LoginController(LoginService loginService, MemberService memberService) {
        this.loginService = loginService;
        this.memberService = memberService;
    }

    // 로그인 폼 화면
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // templates/login.html
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Member loginMember = loginService.login(email, password); 
        // Member 타입 Member 클래스의 객체를 담기 위한 타입
        if (loginMember == null) {
            model.addAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login"; // 로그인 실패 시 로그인 페이지로 돌아감
        }

        // 로그인 성공
        session.setAttribute("loginMember", loginMember); // 로그인된 회원을 세션에 저장
        log.info("로그인 성공: {}", loginMember);
        return "redirect:/"; // 로그인 후 홈으로 리디렉션
    } 

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 제거
        log.info("로그아웃 처리 완료");
        return "redirect:/"; // 로그아웃 후 홈으로 리디렉션
    }

    // 세션에 로그인 정보가 있는지 확인 (로그인 상태 체크용)
    @ModelAttribute("loginMember")
    public Member loginMember(HttpSession session) {
        return (Member) session.getAttribute("loginMember"); // 세션에서 로그인된 회원 정보 반환
    }
}
