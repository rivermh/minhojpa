package com.minhojpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Post;
import com.minhojpa.repository.CommentRepository;

@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	//댓글 저장
	public void saveComment(Comment comment) {
		commentRepository.save(comment);
	}
	
	//특정 게시글에 대한 댓글 조회
	public List<Comment> findCommentsByPost(Post post){
		return commentRepository.findByPost(post);
	}
}
