package com.minhojpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Member;
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
	
	//페이징 처리
	public Page<Post> findAll(Pageable pageable){
		return postRepository.findAll(pageable);
	}
	
	//검색
	public Page<Post> searchPosts(String keyword, Pageable pageable){
		return postRepository.searchByTitleOrContent(keyword, keyword, pageable);
	}
	
	//로그인한 사용자의 게시글 목록
	public List<Post> findPostsByWriter(Member writer){
		return postRepository.findByWriter(writer);
	}
}
