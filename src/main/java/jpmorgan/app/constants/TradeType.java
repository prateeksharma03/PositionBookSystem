package jpmorgan.app.constants;

import jpmorgan.app.constants.TradeType;
import jpmorgan.app.exception.InvalidRequestException;

public enum TradeType {

	BUY, SELL, CANCEL;
	
	public static TradeType getTradeType(String input) {

		for (TradeType tradeType : TradeType.values()) {
			if (tradeType.name().equals(input)) {
				return tradeType;
			}
		}
		throw new InvalidRequestException(String.format(PositionBookConstants.MSG_TRADE_TYPE_INVALID, input));
	}
}
