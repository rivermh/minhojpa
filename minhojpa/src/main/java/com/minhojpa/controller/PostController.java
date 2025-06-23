package com.minhojpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.service.PostLikeService;
import com.minhojpa.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	private final PostService postService;
	private final PostLikeService postLikeService;

	@Autowired
	public PostController(PostService postService, PostLikeService postLikeService) {
		this.postService = postService;
		this.postLikeService = postLikeService;
	}

	// 게시글 목록
	@GetMapping("/posts")
	public String listPosts(@RequestParam(defaultValue = "0") int page,
							@RequestParam(required = false) String keyword,
							Model model,
							HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Pageable pageable = PageRequest.of(page,  10); //페이지 번호, 사이즈(10개씩)
		Page<Post> postPage;
		// Page<Post> postPage = postService.findAll(pageable); 
		
		if(keyword != null&& !keyword.trim().isEmpty()) {
			//검색어가 있을 경우 검색
			postPage = postService.searchPosts(keyword, pageable);
		}else {
			//검색어가 없으면 전체 목록
			postPage = postService.findAll(pageable);
		}
		
		// List<Post> posts = postService.findAll();
		// model.addAttribute("posts", posts); 
		model.addAttribute("postPage", postPage);
		model.addAttribute("posts", postPage.getContent());//실제 게시글 목록
		model.addAttribute("currentPage", page); //현재 페이지 번호
		
		return "postList";
	}

	// 게시글 작성 폼
	@GetMapping("/posts/new")
	public String showCreateForm(Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		model.addAttribute("post", new Post()); 
		return "createPost"; 
	}

	// 게시글 작성 처리
	@PostMapping("/posts")
	public String createPost(@ModelAttribute Post post, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
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
	    Member loginMember = (Member) session.getAttribute("loginMember");
	    if (loginMember == null) {
	        return "redirect:/login";
	    }

	    Post post = postService.findById(id);
	    if (post == null) {
	        return "redirect:/posts";
	    }

	    // 댓글 목록
	    List<Comment> comments = post.getComments(); // 양방향 관계

	    // 좋아요 여부 & 수 추가
	    boolean liked = postLikeService.isLikedByMember(loginMember, post);
	    long likeCount = postLikeService.countLikes(post);

	    // 모델에 담기
	    model.addAttribute("post", post);
	    model.addAttribute("comments", comments);
	    model.addAttribute("liked", liked);
	    model.addAttribute("likeCount", likeCount);
	    model.addAttribute("session", session);

	    return "postDetail";
	}
	
	//게시글 수정 폼
	@GetMapping("/posts/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:login";
		}
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
			return "redirect:/posts";
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
	
	//좋아요
	@PostMapping("/posts/{postId}/like")
	public String toggleLike(@PathVariable Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Post post  = postService.findById(postId); //기존에 있는 메서드라 가정
		boolean liked = postLikeService.toggleLike(loginMember, post);
		redirectAttributes.addFlashAttribute("message", liked ? "종아요 했습니다!" : "좋아요를 취소했습니다.");
		return "redirect:/posts/" + postId;
	}
	
}