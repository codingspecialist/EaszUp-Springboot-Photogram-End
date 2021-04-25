package com.cos.photogram.utils;

import java.util.ArrayList;
import java.util.List;

import com.cos.photogram.domain.image.Image;
import com.cos.photogram.domain.tag.Tag;

public class TagUtils {

	public static List<Tag> parsingToTagObject(String tags, Image imageEntity){
		String temp[] = tags.split("#"); // #여행 #바다
		List<Tag> list = new ArrayList<>();
	
		// 도라에몽 : 파싱할 때 0번지에 공백이 들어와서 시작번지를 1로 함.
		for (int i=1; i<temp.length; i++) {
			Tag tag = Tag.builder()
					.name(temp[i].trim())
					.image(imageEntity)
					.build();
			list.add(tag);
		}
		
		return list;
	}
}
