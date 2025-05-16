package com.minhojpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Post;
import com.minhojpa.repository.PostRepository;

@Service
public class PostService {
	
	private final PostRepository postRepository;
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	//게시글 저장
	public void save(Post post) {
		postRepository.save(post);
	}
	
	//전체 게시글 조회
	public List<Post> findAll(){
		return postRepository.findAll();
	}
	
	//ID로 게시글 조회
	public Post findById(Long id) {
		return postRepository.findById(id).orElse(null);
	}
	
	//게시글 삭제
	public void deleteById(Long id) {
		postRepository.deleteById(id);
	}
}
