package edu.og.project.trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.common.dto.Trade;
import edu.og.project.trade.service.TradeService;

@Controller
@RequestMapping("/trade")
public class TradeController {
	
	@Autowired
	private TradeService service;

	//채팅방에서 거래완료 후 TRADE 테이블에 데이터 삽입
	@PostMapping("/complete")
	@ResponseBody
	public int tradeComplete(@RequestBody Trade trade) {
		
		return service.tradeComplete(trade);
	}
}
