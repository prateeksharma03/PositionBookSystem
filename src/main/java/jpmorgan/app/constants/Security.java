package jpmorgan.app.constants;

import jpmorgan.app.constants.Security;
import jpmorgan.app.exception.InvalidRequestException;

public enum Security {

	SECURITY1, SECURITY2, SECURITY3, SECURITY4, SECURITY5;

	public static Security getSecurity(String input) {
		for (Security security : Security.values()) {
			if (security.name().equals(input)) {
				return security;
			}
		}
		throw new InvalidRequestException(String.format(PositionBookConstants.MSG_SECURITY_INVALID, input));
	}
}
