package com.cos.photogram.domain.user;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.photogram.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@Column(length = 30, unique = true)
	private String username;
	private String password;
	
	private String name; // 이름
	private String website; // 자기 홈페이지
	private String bio; // 자기소개
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl;
	private String provider; // 제공자 Google, Facebook, Naver
	
	private String role; // USER, ADMIN
	
	@OneToMany(mappedBy = "user")
	private List<Image> images;
	
	@CreationTimestamp
	private Timestamp createDate;
;
}
