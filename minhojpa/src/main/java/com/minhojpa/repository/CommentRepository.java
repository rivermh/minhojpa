package com.minhojpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	//특성 게시글(post)에 달린 댓글 리스트 조회
	List<Comment> findByPost(Post post);
	
	//Member 기준으로 댓글 찾기
	List<Comment> findByWriter(Member member); 
}
 