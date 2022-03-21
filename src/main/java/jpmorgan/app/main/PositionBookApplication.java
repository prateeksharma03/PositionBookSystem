package jpmorgan.app.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import jpmorgan.app.constants.PositionBookConstants;
import jpmorgan.app.exception.InvalidRequestException;
import jpmorgan.app.service.PositionBookAppService;

public class PositionBookApplication {

	public static void main(String[] args) throws Exception {
		try (InputStreamReader in = new InputStreamReader(System.in); BufferedReader buffer = new BufferedReader(in)) {
			String line;
			while ((line = buffer.readLine()) != null) {
				try {
					String[] input = line.split("\\s");
					if (input.length == 1) {//get all holdings for given account
						System.out.println(PositionBookAppService.getUserPositionHoldings(input));
					} else if(input.length == 2) {// get holdings for given account and security
						System.out.println(PositionBookAppService.getSecurityPositionHoldings(input));
					} else if (input.length == 5) {// process event trade request
						System.out.println(PositionBookAppService.processTradeEvent(input));
					} else {
						System.out.println(PositionBookConstants.MSG_INVALID_INPUT);
					}
				} catch (InvalidRequestException ire) {
					System.err.println(String.format(PositionBookConstants.MSG_ERR_MSG, ire.getMessage()));
				}

			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	
}