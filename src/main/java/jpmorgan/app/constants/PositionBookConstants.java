package jpmorgan.app.constants;

public final class PositionBookConstants {
	
	private PositionBookConstants() {}

	public static final String MSG_SECURITY_INVALID = "Security %s is invalid ";
	public static final String MSG_TRADE_TYPE_INVALID = "Trade Type  %s is invalid ";
	public static final String MSG_ACC_NOT_FOUND = "Account %s not found ";
	public static final String MSG_ERR_MSG = "Exception occurred :: %s ";
	public static final String MSG_INVALID_INPUT = 
			"Please add input in the given format, either to process trading or to get position for a user's holdings ";
	public static final String MSG_QTY_INVALID = "Quantity %s is invalid ";
	public static final String MSG_EVENT_ID_INVALID = "Event Id %s is invalid ";
	public static final String MSG_NO_HOLDINGS = "%s has no position holding yet!";
	public static final String MSG_CANCEL_DENIED = "Can not cancel or sell order which is not placed! for trade id %s";
	public static final String MSG_EVENT_PROCESSED = "== Event processed for given input ==";
	public static final String MSG_TRADE_EVENT_NOT_FOUND = "Can not cancel, corrosponding event id  %s not found ";
	public static final String MSG_DUPLICATE_EVENT_FOUND = "Duplicate event request found for event id %s";
	
	
}
