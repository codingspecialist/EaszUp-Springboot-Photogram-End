package com.cos.photogram.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogram.domain.image.Image;
import com.cos.photogram.domain.user.User;

import lombok.Data;

@Data
public class ImageReqDto {

	private MultipartFile file;
	private String caption;
	private String tags;
	
	public Image toEntity(String postImageUrl, User userEntity) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(userEntity)
				.build();
	}
}
