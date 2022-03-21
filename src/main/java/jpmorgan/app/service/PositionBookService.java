package jpmorgan.app.service;

import java.util.HashMap;
import java.util.Map;

import jpmorgan.app.dto.TradeEvent;
import jpmorgan.app.constants.PositionBookConstants;
import jpmorgan.app.constants.Security;
import jpmorgan.app.dto.Account;
import jpmorgan.app.exception.InvalidRequestException;

public class PositionBookService {

	private Map<Account, PositionRecord> positionbook = new HashMap<>();

	public String getAllPositions(Account user) {
		PositionRecord positionRecord = positionbook.get(user);
		if (positionRecord == null)
			return String.format(PositionBookConstants.MSG_NO_HOLDINGS, user);
		return user.toString() + "\n" + positionRecord.toString();
	}
	
	public String getPositionBySecurity(Account user, Security security) {
		PositionRecord positionRecord = positionbook.get(user);
		
		if (positionRecord == null)
			return String.format(PositionBookConstants.MSG_NO_HOLDINGS, user);
		return user.toString() + " "+security+" " + positionRecord.getPositionHolding(security);
	}

	public void addRecord(TradeEvent tradeEvent) {
		if (tradeEvent.getQuantity() < 0) {
			throw new InvalidRequestException(
					String.format(PositionBookConstants.MSG_QTY_INVALID, tradeEvent.getQuantity()));
		}
		PositionRecord positionRecord = positionbook.get(tradeEvent.getUser());
		if (positionRecord == null) {
			positionRecord = new PositionRecord();
			positionbook.put(tradeEvent.getUser(), positionRecord);
		}
		positionRecord.processRecord(tradeEvent);
	}
}
