package com.minhojpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Member;
import com.minhojpa.repository.MemberRepository;

@Service
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
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
	
	// 회원 저장
	public Member saveMember(Member member) {
		if(memberRepository.findByEmail(member.getEmail()).isPresent()) {
			throw new IllegalStateException("이미 존재하는 이메일입니다.");
		}
		//비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		return memberRepository.save(member); 
	}
	
	//이메일 존재 여부
	public boolean existsByEmail(String email) {
	    return memberRepository.findByEmail(email).isPresent();
	}

	
	// 마이페이지 회원 수정
	public void updateSelf(Long id, String name, String email, String password) {
		Optional<Member> optionalMember = memberRepository.findById(id);
		if(optionalMember.isPresent()) {
			Member member = optionalMember.get();
			member.setName(name);
			member.setEmail(email);
			
			//수정시에도 암호화
			String encodedPassword = passwordEncoder.encode(password);
			member.setPassword(encodedPassword);
			memberRepository.save(member);
		}else {
			
		}
	}
}

//optional
//db에서 해당 id의 member가 있으면 optional<member> 안에 감싸서 리턴, 없으면 optional.empty() 반환

//member.orElse(null)
//값이 있으면 꺼내고, 없으면 null 리턴
//npe방지를 위해 
