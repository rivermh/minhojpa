 package com.minhojpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Member;
import com.minhojpa.repository.MemberRepository;

@Service // 이 클래스가 서비스 로직을 처리하는 클래스임 컨트롤러에서 넘어온 요청을 받아서, 진짜 DB에 가기 전에 로직 처리해주는 부분
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	@Autowired // 생성자 주입을 통해 repository를 주입
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	// 모든 회원 조회
	public List<Member> findAllmembers(){
		return memberRepository.findAll();
	}
	
	// ID로 회원 조회
	public Member findMemberById(Long id) {
		Optional<Member> member = memberRepository.findById(id); // Member 객체가 있을 수도, 없을 수도 있는 타입
		return member.orElse(null); // 만약 회원이 없다면 null 반환
	}
	
	// 회원 저장
	public Member saveMember(Member member) {
		return memberRepository.save(member);
	}
	
	// 회원 삭제
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}
}
