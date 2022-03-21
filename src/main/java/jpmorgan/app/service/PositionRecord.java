package jpmorgan.app.service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jpmorgan.app.constants.PositionBookConstants;
import jpmorgan.app.constants.Security;
import jpmorgan.app.constants.TradeType;
import jpmorgan.app.dto.TradeEvent;
import jpmorgan.app.exception.InvalidRequestException;

public class PositionRecord {

	private Map<Security, Set<TradeEvent>> tradeMap;
	private Map<Security, Integer> positionHolding;

	public PositionRecord() {
		tradeMap = new HashMap<Security, Set<TradeEvent>>();
		positionHolding = new HashMap<>();
	}

	public void processRecord(TradeEvent tradeEvent) {
		Set<TradeEvent> tradeSet = tradeMap.get(tradeEvent.getSecurity());
		if (tradeSet == null) {
			tradeSet = new LinkedHashSet<TradeEvent>();
			tradeMap.put(tradeEvent.getSecurity(), tradeSet);
		}
		if(tradeSet.add(tradeEvent)) {//To ensure duplicate event request can not be processed
			processEvent(tradeEvent);
		} else {
			throw new InvalidRequestException(String.format(PositionBookConstants.MSG_DUPLICATE_EVENT_FOUND, tradeEvent.getId()));
		}
		
	}

	public void processEvent(TradeEvent tradeEvent) {
		if (positionHolding.containsKey(tradeEvent.getSecurity())) {
			Integer position = positionHolding.get(tradeEvent.getSecurity());
			if (tradeEvent.getTradeType().equals(TradeType.BUY)) {
				position += tradeEvent.getQuantity();
			} else if (tradeEvent.getTradeType().equals(TradeType.SELL)) {
				position -= tradeEvent.getQuantity();// Can be negative in case of short sell
			} else if (tradeEvent.getTradeType().equals(TradeType.CANCEL)) {
				Optional<TradeEvent> previousEvent = tradeMap.get(tradeEvent.getSecurity()).stream()
						.filter(event -> event.getId() == tradeEvent.getId()).findAny();
				if (previousEvent.isPresent()) {// Previous event which we are going to cancel could be either buy or sell
					if (previousEvent.get().getTradeType().equals(TradeType.BUY)) {
						position -= previousEvent.get().getQuantity();// if previous event was buy then reduce total holdings
					} else if (previousEvent.get().getTradeType().equals(TradeType.SELL)) {
						position += previousEvent.get().getQuantity();// if previous event was sell then increase total holdings
					}
				} else {
					throw new InvalidRequestException(
							String.format(PositionBookConstants.MSG_TRADE_EVENT_NOT_FOUND, tradeEvent.getId()));
				}
			} else {// In case of cancel if given event id doesn't exist then throw exception
				throw new InvalidRequestException(
						String.format(PositionBookConstants.MSG_TRADE_TYPE_INVALID, tradeEvent.getTradeType()));

			}
			positionHolding.put(tradeEvent.getSecurity(), position);//Update position holdings
		} else if (tradeEvent.getTradeType().equals(TradeType.BUY)) {
			positionHolding.put(tradeEvent.getSecurity(), tradeEvent.getQuantity());
		} else {
			//In case of cancel and sell request if order does not exist throw error
			throw new InvalidRequestException(
					String.format(PositionBookConstants.MSG_CANCEL_DENIED, tradeEvent.getId()));

		}

	}

	public Integer getPositionHolding(Security security) {
		return (positionHolding.get(security) == null) ? 0 : positionHolding.get(security);
	}

	public Set<TradeEvent> getTradeList(Security security) {
		return tradeMap.get(security);
	}

	public Map<Security, Set<TradeEvent>> getTradeMap() {
		return this.tradeMap;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<Security, Set<TradeEvent>> entry : tradeMap.entrySet()) {
			result.append(entry.getKey());
			result.append(" " + this.positionHolding.get(entry.getKey()) + " \n");
			result.append(entry.getValue().toString());
			result.append("\n");
		}
		return result.toString();
	}

}
