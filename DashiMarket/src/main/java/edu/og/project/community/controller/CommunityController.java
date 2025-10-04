package edu.og.project.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityController {

	@GetMapping("/{boardType:c.*}")
	public String selectCommunityList() {
		return "communityPage/communityHome";
	}
}
