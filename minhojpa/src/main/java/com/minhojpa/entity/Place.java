package com.minhojpa.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 가게 이름
    private String category;    // 카페, 식당, 분식, etc
    
    private String roadAddress;     // 도로명 주소
    private String jibunAddress;    // 지번 주소
    private String detailAddress;   // 건물명, 가게명
    private String postcode;        // 우편번호

    private Double latitude;    // 위도
    private Double longitude;   // 경도

    private boolean soloFriendly;   // 혼밥 가능 여부
    private boolean quickServe;     // 빨리 나옴
    private boolean noPressure;     // 눈치 없음
    private boolean openLate;       // 자정 이후 운영

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();  // 해당 장소에 대한 게시글들
    
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
    @Column(unique = true)
    private String kakaoPlaceId; // 카카오 API에서의 place_id

}

	


