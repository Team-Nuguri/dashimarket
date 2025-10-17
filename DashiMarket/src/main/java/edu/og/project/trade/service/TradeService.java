package edu.og.project.trade.service;

import edu.og.project.common.dto.Trade;

public interface TradeService {

	/** 거래완료 - trade table insert
	 * @param trade
	 * @return result -> 성공:1, 실패:0
	 */
	int tradeComplete(Trade trade);

}
