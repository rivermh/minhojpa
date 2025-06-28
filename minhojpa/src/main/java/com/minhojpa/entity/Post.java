package com.minhojpa.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 이 클래스가 JPA의 엔티티임을 나타냄 (DB의 테이블과 매핑됨)
@Getter // 모든 필드에 대해 Getter 메서드 자동 생성(Lombok)
@Setter // 모든 필드에 대해 Setter 메서드 자동 생성(Lombok)
@ToString // toString() 메서드 자동 생성(디버깅 시 객체 정보 출력에 유용)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동 증가 사용 (MySQL의 AUTO_INCREMENT)
	private Long id; // 게시글 번호 (기본키)
	
	private String title; //게시글 제목
	
	@Lob // 긴 텍스트 데이터 처리 (Large Object) - 게시글 내용
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정 (여러 게시글이 한 회원에게 작성됨)
	@JoinColumn(name = "member_id") // 외래 키 컬럼 이름 설정
	private Member writer; // 게시글 작성자 (Member 엔티티와 연결됨)
	
	@CreationTimestamp // 엔티티가 저장될 때 자동으로 현재 시간 설정
	private LocalDateTime createdAt; // 게시글 작성 시간
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;
}
