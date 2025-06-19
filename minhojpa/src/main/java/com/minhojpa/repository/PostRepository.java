package com.minhojpa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	// 기본적인 CRUD 메서드 (save, findAll, findById 등)는 자동 제공됨	
	Page<Post> findAll(Pageable pageable); // 기본 페이징 메서드
		
	// 제목 또는 내용으로 검색 
	Page<Post> searchByTitleOrContent(String title, String content, Pageable pageable);
	
	List<Post> findByWriter(Member writer); // 로그인한 사용자의 게시글 조회
}
