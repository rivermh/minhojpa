package com.minhojpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Member;
import com.minhojpa.repository.MemberRepository;

@Service
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	// 모든 회원 조회
	public List<Member> findAllmembers(){
		return memberRepository.findAll();
	}
	
	// ID로 회원 조회
	public Member findMemberById(Long id) {
		Optional<Member> member = memberRepository.findById(id);
		return member.orElse(null);
	}
	
	// 회원 삭제
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}
	
	// 회원 저장 (비밀번호 암호화 없이)
	public Member saveMember(Member member) {
		return memberRepository.save(member); // 비밀번호 암호화 없이 저장
	}
	
	// 마이페이지 회원 수정
	public void updateSelf(Long id, String name, String email, String password) {
		Optional<Member> optionalMember = memberRepository.findById(id);
		if(optionalMember.isPresent()) {
			Member member = optionalMember.get();
			member.setName(name);
			member.setEmail(email);
			member.setPassword(password);
			memberRepository.save(member);
		}
	}
}
