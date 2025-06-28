package com.minhojpa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Column(nullable = false)
	private String content;
	
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
	//@ManyToOne 어노테이션은 이 필드는 외래키 관계 라고 JPA에게 알려준다
	//JPA는 post 객체에서 @Id로 지정된 값을 찾아 외래키에 매핑한다.
	//이 전체 흐름은 JPA 내부 매핑 엔진(Hibernate 등)이 알아서 처리한다
	//JoinColumn은 외래키 컬럼을 만든다
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member writer; // 의미 : 이 댓글의 작성자
}

/*
 @Entity JPA가 관리하는 엔티티 클래스임을 명시
 @Id, @GeneratedValue 기본 키 자동 생성
 @Column(nullable = false) 댓글 내용은 반드시 입력되어야 함
 createdAt 댓글 생성 시간(현재 시간으로 기본 설정
 @ManyToOne 댓글은 하나의 게시글과 작성자를 가짐(Post, Member 와 다대일 관계)
 @fetch = FetchType.LAZY 지연 로딩 설정(댓글을 조회할 때 post나 member는 필요할 때만 로딩*/
