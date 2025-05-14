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
    // MemberService 타입의 객체를 주입받기 위한 필드
    // final로 선언하여 생성자에서 반드시 초기화되도록 강제

    @Autowired // 스프링이 MemberService 빈을 주입해줌 (DI)
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 홈 페이지
    @GetMapping("/") // http://localhost:8080/
    public String home(HttpSession session, Model model) {
    	// HttpSession : 현재 로그인한 사용자의 세션 정보를 가져올 수 있는 객체 ( 예: 로그인 여부 확인 등)
    	// Model : 컨트롤러에서 뷰로 데이터를 전달할 때 사용하는 객체
    	Member loginMember = (Member) session.getAttribute("loginMember");
    	if(loginMember != null) {
    		model.addAttribute("loginMember", loginMember); // loginMember라는 이름으로 뷰에 전달
    	}
        return "home"; // templates/home.html 반환
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

/*@RequestParam은 HTTP 요청 파라미터를 메서드의 파라미터에 바인딩해주는 어노테이션
주로 HTML 폼 또는 URL 쿼리스트링의 name 속성과 매칭하여 값을 가져옴
예: /hello?name=Minho → @RequestParam("name") String name 에 "Minho" 값이 자동으로 들어옴
파라미터 이름과 변수명이 같다면 ("name") 생략도 가능하며, 기본값이나 필수 여부도 설정할 수 있음
*/

/*
HttpSession session
현재 접속한 사용자와 관련된 세션 정보를 가져오는 매개변수
로그인 사용자 정보를 session에 저장해두고, 다른 요청에서 이를 꺼내 쓸 수 있음
예: session.getAttribute("loginMember")로 로그인 사용자 정보 가져오기

Model model
컨트롤러에서 뷰로 데이터를 전달할 때 사용하는 객체
model.addAttribute("key", value) 형식으로 데이터 전달
 */

/*
MemberService는 비즈니스 로직(회원 저장, 조회 등)을 처리하는 서비스 클래스
이 필드는 컨트롤러가 서비스에 접근하기 위해 선언한 의존성이다
final로 선언하여 생성자에서 반드시 주입받도록 보장함
@Autowired가 붙은 생성자를 통해 스프링이 자동으로 주입해준다.
*/

//@GetMapping
//@PostMapping
//@PathVariable