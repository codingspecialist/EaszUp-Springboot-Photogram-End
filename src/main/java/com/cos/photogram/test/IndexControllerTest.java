package com.cos.photogram.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControllerTest {

	@GetMapping("/test")
	public String index() {
		return "index";
	}
}
