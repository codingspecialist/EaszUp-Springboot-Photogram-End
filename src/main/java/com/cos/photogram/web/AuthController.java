package com.cos.photogram.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogram.service.AuthService;
import com.cos.photogram.utils.Script;
import com.cos.photogram.web.dto.auth.UserJoinReqDto;

import lombok.RequiredArgsConstructor;

// 시작주소 : /auth
@RequiredArgsConstructor
@Controller
public class AuthController {

	private final AuthService authService;

	@GetMapping("/auth/signin")
	public String loginForm() {
		return "auth/signin";
	}

	@GetMapping("/auth/signup")
	public String joinForm() {
		return "auth/signup";
	}

	@PostMapping("/auth/signup")
	public @ResponseBody String join(UserJoinReqDto userJoinReqDto) {
		authService.회원가입(userJoinReqDto.toEntity());
		return Script.href("회원가입에 성공하셨습니다.", "/auth/signin");
	}
}
