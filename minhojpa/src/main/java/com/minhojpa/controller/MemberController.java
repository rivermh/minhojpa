package com.minhojpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.minhojpa.entity.Member;
import com.minhojpa.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller // 이 클래스가 웹 요청을 처리하는 컨트롤러임을 나타냄
@Slf4j // 로그 출력을 위한 Lombok 어노테이션
// HTTP 요청 흐름: Controller → Service → Repository → DB → Service → Controller → 응답(View 또는 JSON)
public class MemberController {

    private final MemberService memberService;

    @Autowired // 스프링이 MemberService 빈을 주입해줌 (DI)
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 홈 페이지
    @GetMapping("/") // http://localhost:8080/
    public String home(HttpSession session, Model model) {
    	Member loginMember = (Member) session.getAttribute("loginMember");
    	
    	if(loginMember != null) {
    		model.addAttribute("loginMember", loginMember);
    	}
        return "home"; // templates/home.html 반환
    }

    // JSON 형식으로 전체 회원 조회 (REST API 용도)
    @GetMapping("/api/members")
    public List<Member> getAllMembersApi() {
        return memberService.findAllmembers();
    }

    // JSON 형식으로 특정 회원 조회
    @GetMapping("/api/members/{id}")
    public Member getMemberByIdApi(@PathVariable Long id) {
        return memberService.findMemberById(id);
    }

    // 전체 회원 조회 (화면용)
    @GetMapping("/members") // http://localhost:8080/members
    public String getAllMembers(Model model) {
        List<Member> members = memberService.findAllmembers(); // 서비스에서 모든 회원 가져옴
        model.addAttribute("members", members); // model에 members 데이터 담음
        return "memberList"; // templates/memberList.html 반환
    }

    // 회원 등록 폼 보여주기
    @GetMapping("/members/new") // http://localhost:8080/members/new
    public String showCreateForm() {
        return "createMember"; // templates/createMember.html 반환
    }

    // 회원 등록 처리
    @PostMapping("/members") // form의 action="/members", method="post"와 매칭
    public String createMember(@RequestParam String name,
    						   @RequestParam String email,
    						   @RequestParam String password) {
        Member member = new Member(); // 새로운 회원 객체 생성
        member.setName(name); // 이름 설정
        member.setPassword(password); //비밀번호 설정
        member.setEmail(email); // 이메일 설정
        memberService.saveMember(member); // 서비스에 저장 요청
        log.info("회원 저장됨 : {}, {}",name, email, password);
        return "redirect:/members"; // 등록 후 회원 목록으로 리디렉션
    }

    // 회원 수정 폼 보여주기
    @GetMapping("/members/edit/{id}") // http://localhost:8080/members/edit/1
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberService.findMemberById(id); // ID로 회원 조회
        model.addAttribute("member", member); // 조회된 회원을 모델에 담음
        return "editMember"; // templates/editMember.html 반환
    }

    // 회원 수정 처리
    @PostMapping("/members/edit")
    public String updateMember(@RequestParam Long id,
    						   @RequestParam String email,
    						   @RequestParam String password,
    						   @RequestParam String name) {
        Member member = memberService.findMemberById(id); // 기존 회원 조회
        member.setName(name);
        member.setPassword(password); // 이름 수정
        member.setEmail(email); // 이메일 수정
        memberService.saveMember(member); // 수정된 회원 저장
        log.info("회원 수정됨: {}", member);
        return "redirect:/members"; // 수정 후 회원 목록으로 리디렉션
    }

    // 회원 삭제 처리
    @GetMapping("/members/delete/{id}") // 삭제는 보통 GET이나 POST로 처리 가능
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id); // 서비스에서 삭제 처리
        log.info("회원 삭제됨: id={}", id);
        return "redirect:/members"; // 삭제 후 목록으로 이동
    }
    
    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember"); // 세션에서 꺼냄
        if (loginMember == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }
        model.addAttribute("member", loginMember); // 뷰에 전달
        return "myPage"; // templates/myPage.html
    }
}

/*@RequestParam은 HTTP 요청 파라미터를 메서드의 파라미터에 바인딩해주는 어노테이션이다.
주로 HTML 폼 또는 URL 쿼리스트링의 name 속성과 매칭하여 값을 가져온다.
예: /hello?name=Minho → @RequestParam("name") String name 에 "Minho" 값이 자동으로 들어온다.
파라미터 이름과 변수명이 같다면 ("name") 생략도 가능하며, 기본값이나 필수 여부도 설정할 수 있다.*/


//@GetMapping
//@PostMapping
//@PathVariable