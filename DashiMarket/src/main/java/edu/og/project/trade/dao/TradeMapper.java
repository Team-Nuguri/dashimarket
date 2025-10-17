package edu.og.project.trade.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Trade;

@Mapper
public interface TradeMapper {

	// 거래완료 insert
	int tradeComplete(Trade trade);

}
