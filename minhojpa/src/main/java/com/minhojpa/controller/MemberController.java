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

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Member;
import com.minhojpa.service.CommentService;
import com.minhojpa.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller // 이 클래스가 웹 요청을 처리하는 컨트롤러임을 나타냄
@Slf4j // 로그 출력을 위한 Lombok 어노테이션
// HTTP 요청 흐름: Controller → Service → Repository → DB → Service → Controller → 응답(View 또는 JSON)
public class MemberController {

    private final MemberService memberService;
    private final CommentService commentService;

    @Autowired // 스프링이 MemberService 빈을 주입해줌 (DI)
    public MemberController(MemberService memberService, CommentService commentService) {
        this.memberService = memberService;
        this.commentService = commentService;
    }

    // 홈 페이지
    @GetMapping("/") // http://localhost:8080/
    public String home(HttpSession session, Model model) {
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
    @GetMapping("/mypage/edit")
    public String showMyEditForm(HttpSession session, Model model) {
    	Member loginMember = (Member) session.getAttribute("loginMember");
    	if(loginMember == null) {
    		return "redirect:/login";
    	}
    	model.addAttribute("member", loginMember); // editMember.html에서 th:field에 바인딩
    	return "editMember";
    }

    // 회원 수정 POST
    @PostMapping("/mypage/edit")
    public String udpateMyInfo(@ModelAttribute Member formMember, HttpSession session) {
    	/*@ModelAttribute를 쓰면 new Member() 객체를 하나 만든 다음 
    	  폼에서 넘어온 값들 (name=~~~~, email=~~~@~~~등)을
    	  setName("~~~"), setEmail("~~~@~~~") 이런 식으로 해당 객체에 setXXX 해줘서 완성된
    	  Member 객체를 만들어서 그걸 formMember 매개변수로 넘겨줌
    	  즉, @modelAttribute는 이미 set까지 완성된 상태에서 formMember에 set이 들어있는 상태
    	*/
    	Member loginMember = (Member) session.getAttribute("loginMember");
    	if(loginMember == null) {
    		return "redirect:/login";
    	} 
    	 
        memberService.updateSelf(loginMember.getId(), formMember.getName(),
                                 formMember.getEmail(), formMember.getPassword());
        
        // 세션 정보 최신화 
        session.setAttribute("loginMember", memberService.findMemberById(loginMember.getId()));
        
        return "redirect:/mypage";
    }
     
    
    // 회원 삭제 처리
    @PostMapping("/mypage/delete")
    public String deleteMyAccount(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        memberService.deleteMember(loginMember.getId());
        session.invalidate(); // 세션 무효화
        return "redirect:/"; // 홈으로 이동
    }

    
    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember"); // 세션에서 꺼냄
        if (loginMember == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }
        
        List<Comment> comments = commentService.findCommentsByWriter(loginMember);
        model.addAttribute("member", loginMember); // 뷰에 전달
       // model.addAttribute("posts", loginMember.getPosts());
        model.addAttribute("comments", loginMember.getComments());
        return "myPage"; // templates/myPage.html
    } 
}

/*
@RequestParam
- HTTP 요청의 쿼리 파라미터를 메서드 파라미터에 바인딩해주는 어노테이션
- 주로 HTML 폼 입력값이나 URL의 쿼리스트링에서 값을 가져올 때 사용
- 예: /hello?name=Minho → @RequestParam("name") String name → "Minho"가 변수에 들어감
- 파라미터 이름과 변수명이 같으면 ("name") 생략 가능
- required, defaultValue 등 추가 설정도 가능
*/

/*
HttpSession session
- 로그인 사용자 정보를 저장하고, 이후 요청에서도 꺼내 쓸 수 있게 해주는 세션 객체
- 예: session.getAttribute("loginMember") → 로그인된 사용자 정보 가져오기
- 세션은 브라우저 단위로 유지되며, 서버에 사용자 상태를 저장할 수 있음
*/

/*
Model model
- 컨트롤러에서 뷰로 데이터를 전달할 때 사용하는 객체
- model.addAttribute("키", 값) 형식으로 데이터를 전달하면
  뷰(HTML, Thymeleaf 등)에서 ${키}로 출력 가능
*/

/*
MemberService
- 회원 관련 비즈니스 로직을 처리하는 서비스 클래스 (저장, 조회, 수정, 삭제 등)
- 컨트롤러에서 호출해 사용하며, final로 선언해 생성자에서 반드시 주입받도록 보장
- @Autowired가 붙은 생성자를 통해 스프링이 자동으로 MemberRepository를 주입
*/

/*
@PathVariable
- URL 경로에 포함된 값을 메서드의 파라미터로 바인딩할 때 사용하는 어노테이션
- 예: /members/delete/1 → @PathVariable Long id → id = 1
- RESTful URL 설계에서 자주 사용됨 (리소스 식별용)
- 경로 변수 이름과 파라미터 이름이 같으면 ("id") 생략 가능
*/
