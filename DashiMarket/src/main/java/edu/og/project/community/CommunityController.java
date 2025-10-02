package edu.og.project.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityController {

	@GetMapping("/community")
	public String forwardComm() {
		return "communityPage/communityHome";
	}
}
