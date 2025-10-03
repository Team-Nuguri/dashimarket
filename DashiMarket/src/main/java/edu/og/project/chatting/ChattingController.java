package edu.og.project.chatting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChattingController {

	@GetMapping("/chatting")
	public String chatting() {
		return "chatting/chatting";
	}
}
