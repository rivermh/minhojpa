package com.minhojpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화는 계속 사용
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 기능을 최소화해서 로그인은 직접 처리하게 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트용)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/logout", "/members/**", "/css/**", "/posts/**", "/mypage/**", "/comments/**").permitAll()
                .anyRequest().permitAll() // 모두 허용
            )
            .formLogin(form -> form.disable()) // 시큐리티 로그인 기능 완전히 비활성화
            .logout(logout -> logout.disable()); // 시큐리티 로그아웃도 비활성화

        return http.build();
    }
}
