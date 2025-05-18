package com.minhojpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	// 게시글 목록
	@GetMapping("/posts")
	public String listPosts(Model model) {
		List<Post> posts = postService.findAll();
		//List 여러 개의 T 객체를 순서 있게 저장할 수 있는 컬렉션 타입 여기서 T는 Post
		model.addAttribute("post", posts); // view에 전달
		return "postList"; // templates/postList.html
	}

	// 게시글 작성 폼
	@GetMapping("/posts/new")
	public String showCreateForm(Model model) {
		model.addAttribute("post", new Post()); //빈 Post 객체를 모델에 담음
		return "createPost"; // templates/createPost.html
	}

	// 게시글 작성 처리
	@PostMapping("/posts")
	public String createPost(@ModelAttribute Post post, HttpSession session) {
		//@ModelAttribute 사용자가 form에서 작성한 입력값(title, content 등)을 Post 객체에 자동으로 담아줌
		//즉 작성 폼 new Post()에서 자동으로 생성하고 폼 데이터를 title, content 값을 Post 객체의 setTitle(), setContent()로 자동 바인딩
		//반드시 setter가 있어야 바인딩 가능 form의 name 속성과 객체의 setter를 기반으로 자동 매핑
		Member loginMember = (Member) session.getAttribute("loginMember");
		//Member로 다시 캐스팅 해준 이유는 HttpSession의 getAttribute() 메서드는 항상 Object 타입으로 리턴 하기 때문
		//또한 HttpSession은 제네릭을 지원하지 않는 구조이기 때문에 넣을 때 타입 정보는 기억 못함
		if (loginMember == null) {
			return "redirect:/login";
		}
		post.setWriter(loginMember); // post 엔터티에서 바로 값 가져옴 그 안에 loginMember 넣어줌
		postService.save(post);
		return "redirect:/posts";
	}

	// 게시글 상세보기
	@GetMapping("/posts/{id}")
	public String viewPost(@PathVariable Long id, Model model, HttpSession session) {
		//@PathVariable URL 속 값을 꺼내서 메서드 파라미터로 넘겨줌 {id} 부분을 변수 id에 자동 바인딩 해줌
		//즉, /posts/5로 접속하면 id = 5로 바인딩되어서 postService.findById(5)가 호출됨
		Post post = postService.findById(id);
		if (post == null) {
			return "redirect:/posts";
		}
		
		//댓글 목록
		List<Comment> comments = post.getComments(); // 양방향 관계

		model.addAttribute("post", post);
		model.addAttribute("comments", comments);
		model.addAttribute("session", session); // Thymeleaf에서 session 쓰려면 필요
		return "postDetail"; // templates/postDetail.html
	}
	
	//게시글 수정 폼
	@GetMapping("/posts/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model) {
		Post post = postService.findById(id);
		if(post == null) {
			return "redirect:/posts"; 
		}
		model.addAttribute("post", post);
		return "editPost";
	}
	
	//게시글 수정 처리하기
	@PostMapping("/posts/{id}/edit")
	public String updatePost(@PathVariable Long id, @ModelAttribute Post updatedPost, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Post post = postService.findById(id);
		if(post == null || !post.getWriter().getId().equals(loginMember.getId())) {
			// 게시글이 없거나, 작성자가 다르면 수정 못 함
		}
		
		//수정 내용 반영
		post.setTitle(updatedPost.getTitle());
		post.setContent(updatedPost.getContent());
		
		postService.save(post); // save는 수정도 됨 (JPA는 같은 ID면 merge 됨)
		return "redirect:/posts/" + id;
	}
	
	//게시글 삭제 처리하기
	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable Long id, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Post post = postService.findById(id);
		if(post == null || !post.getWriter().getId().equals(loginMember.getId())) {
			return "redirect:/posts";
		}
		
		postService.deleteById(id);
		return "redirect:/posts";
	}
}