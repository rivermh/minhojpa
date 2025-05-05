package com.minhojpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minhojpa.entity.Member;
import com.minhojpa.service.MemberService;

@RestController // 이 클래스가 RESTAPI 처리용 컨트롤러임을 나타냄
@RequestMapping("/members") // /members로 시작하는 URL 요청을 이 컨트롤러에서 처리하겠다는 의미
//HTTP 요청 Controller > Service > Repository > DB에서 데이터 가져옴 > Service > Controller > HTTP 응답 (JSON 등)
public class MemberController {
	
	private final MemberService memberService;
	
	@Autowired //서비스 객체를 받아와서 내부에서 사용할 수 있게 해줌
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	//전체 회원 조회
	@GetMapping // GET 요청 모든 회원을 리스트로 반환
	public List<Member> getAllMember(){
		return memberService.findAllmembers();
	}
	
	//특정 회원 조회
	@GetMapping("/{id}") // /members/1 이런 요청이 들어오면 1을 @PathVariable로 받아서 조회
	public Member getMemberById(@PathVariable Long id) { //이 URL에 있는 ID 값을 이용해서, 그 ID에 해당하는 Member를 찾아서 반환
		return memberService.findMemberById(id);
	}
	
	// 회원 저장
	@PostMapping //POST 요청으로 JSON 형식의 회원 데이터를 넘기면, 그걸 @RequstBody로 받아서 저장
	public Member createMember(@RequestBody Member member) {
		return memberService.saveMember(member);
	}
}
