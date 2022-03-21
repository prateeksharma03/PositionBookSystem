package jpmorgan.app.service;

import java.util.HashMap;
import java.util.Map;

import jpmorgan.app.constants.PositionBookConstants;
import jpmorgan.app.constants.Security;
import jpmorgan.app.constants.TradeType;
import jpmorgan.app.dto.TradeEvent;
import jpmorgan.app.dto.Account;
import jpmorgan.app.exception.InvalidRequestException;

public class PositionBookAppService {

	public static PositionBookService positionBook = new PositionBookService();
	public static Map<Account, Security> accountMap = new HashMap<>();

	public static String getUserPositionHoldings(String[] input) {
		Account account = new Account(input[0]);
		if (accountMap.size() == 0 || !accountMap.containsKey(account)) {
			throw new InvalidRequestException(String.format(PositionBookConstants.MSG_ACC_NOT_FOUND, input[0]));
		} else {
			return positionBook.getAllPositions(account);
		}
	}
	
	public static String getSecurityPositionHoldings(String[] input) {
		Account account = new Account(input[0]);
		Security security = getSecurity(input[1]);
		if (accountMap.size() == 0 || !accountMap.containsKey(account)) {
			throw new InvalidRequestException(String.format(PositionBookConstants.MSG_ACC_NOT_FOUND, input[0]));
		} else {
			return positionBook.getPositionBySecurity(account, security);
		}
	}

	public static String processTradeEvent(String[] input) {
		Account account = new Account(input[2]);
		TradeType tradeType = getTradeType(input[1]);
		long tradeEventId = getTradeEventId(input[0]);
		Security security = getSecurity(input[3]);
		int qty = getQuantity(input[4]);
		accountMap.put(account, security);
		TradeEvent tradeEvent = new TradeEvent(tradeEventId, tradeType, security, qty, account);
		positionBook.addRecord(tradeEvent);
		return getPositionBook();
	}

	public static String getPositionBook() {
		String positionBookData = "";
		for (Map.Entry<Account, Security> entry : accountMap.entrySet()) {
			positionBookData += positionBook.getAllPositions(entry.getKey());
		}
		return positionBookData;
	}

	public static TradeType getTradeType(String inputTradeType) {
		return TradeType.getTradeType(inputTradeType);
	}

	public static Security getSecurity(String inputSecurity) {
		return Security.getSecurity(inputSecurity);
	}
	
	public static int getQuantity(String quantity) {
		try {
			return Integer.parseInt(quantity);
		} catch (NumberFormatException e) {
			throw new InvalidRequestException(String.format(PositionBookConstants.MSG_QTY_INVALID, quantity));
		}
	}
	public static long getTradeEventId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			throw new InvalidRequestException(String.format(PositionBookConstants.MSG_EVENT_ID_INVALID, id));
		}
	}
}