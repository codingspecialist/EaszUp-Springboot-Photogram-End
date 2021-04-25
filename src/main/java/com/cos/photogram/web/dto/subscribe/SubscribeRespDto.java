package com.cos.photogram.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeRespDto {
	private int userId;
	private String username;
	private String profileImageUrl;
	private Integer subscribeState; // mariadb에서는 Integer
	private Integer equalState;
}
