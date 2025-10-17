package edu.og.project.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.common.dto.Trade;
import edu.og.project.trade.dao.TradeMapper;

@Service
public class TradeServiceImpl implements TradeService{
	
	@Autowired
	private TradeMapper mapper;

	@Override
	public int tradeComplete(Trade trade) {
		
		int result = mapper.tradeComplete(trade);
		
		if(result == 0) {
			return 0;
		}
		
		return result;
	}

}
