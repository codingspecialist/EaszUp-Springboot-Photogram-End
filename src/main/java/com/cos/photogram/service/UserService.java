package com.cos.photogram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.domain.follow.SubscribeRepository;
import com.cos.photogram.domain.user.User;
import com.cos.photogram.domain.user.UserRepository;
import com.cos.photogram.web.dto.user.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public User 회원사진변경(MultipartFile profileImageFile, PrincipalDetails principalDetails) {

		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();
		// System.out.println("파일명 : "+imageFileName);

		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		// System.out.println("파일패스 : "+imageFilePath);
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		User userEntity = userRepository.findById(principalDetails.getUser().getId()).get();
		userEntity.setProfileImageUrl(imageFileName);

		return userEntity;
	} // 더티체킹

	@Transactional
	public User 회원수정(int id, User user) {
		// username, email 수정 불가
		User userEntity = userRepository.findById(id).get();

		userEntity.setName(user.getName());
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setGender(user.getGender());
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);

		// 비밀번호를 수정시 안넘기면 기존 비번 유지
		if(!user.getPassword().equals("")) {
			userEntity.setPassword(encPassword);
		}
	
		
		return userEntity;
	} // 더티체킹

	@Transactional(readOnly = true)
	public UserProfileRespDto 회원프로필(int userId, int principalId) {
		UserProfileRespDto userProfileRespDto = new UserProfileRespDto();

		User userEntity = userRepository.findById(userId).orElseThrow(() -> {
			return new IllegalArgumentException();
		});

		int subscribeState = subscribeRepository.mSubscribeState(principalId, userId);
		int subscribeCount = subscribeRepository.mSubscribeCount(userId);

		userProfileRespDto.setSubscribeState(subscribeState == 1);
		userProfileRespDto.setSubscribeCount(subscribeCount); // 내가 팔로우 하고 있는 카운트
		userProfileRespDto.setImageCount(userEntity.getImages().size());

		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});

		userProfileRespDto.setUser(userEntity);

		return userProfileRespDto;
	}
}
