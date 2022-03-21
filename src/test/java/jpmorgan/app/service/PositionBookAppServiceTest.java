package jpmorgan.app.service;


import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jpmorgan.app.exception.InvalidRequestException;

class PositionBookAppServiceTest {

	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
		PositionBookAppService.positionBook = new PositionBookService();
		PositionBookAppService.accountMap = new HashMap<>();
	}
	
	/**
	 * Parameterized test case to verify input data validations
	 * 
	 * @param input
	 * @param expectedErrorMsg
	 */
	@ParameterizedTest
	@MethodSource("processTradeEventRequestValidationTestData")
	final void testProcessTradeEventShouldThrowErrorInvalidEventId(String[] input, String expectedErrorMsg) {
		try {
			PositionBookAppService.processTradeEvent(input);
		} catch (InvalidRequestException e) {
			Assertions.assertEquals(true, e.getMessage().contains(expectedErrorMsg));
			
		}
	}
	
	private static Stream<Arguments> processTradeEventRequestValidationTestData() {
	    return Stream.of(
	      Arguments.of(new String[] { "abc", "BUY", "ACC1", "SECURITY1", "100" }, "Event Id abc is invalid"),
	      Arguments.of(new String[] { "4", "BUY", "ACC1", "SECURITY1", "abs" }, "Quantity abs is invalid"),
	      Arguments.of(new String[] { "5", "BUY", "ACC1", "SE1", "10" }, "Security SE1 is invalid"),
	      Arguments.of(new String[] { "6", "BUY", "ACC2", "SECURITY1", "100" }, "Account ACC2 not found"),
	      Arguments.of(new String[] { "7", "BOUGHT", "ACC1", "SECURITY1", "100" }, "BOUGHT is invalid")
	    );
	}

	/**
	 * Test case to validate users holdings
	 */
	@Test
	final void testGetUserPositionHoldings() {
		PositionBookAppService.processTradeEvent(new String[] { "1", "BUY", "ACC1", "SECURITY1", "100" });
		PositionBookAppService.processTradeEvent(new String[] { "2", "BUY", "ACC1", "SECURITY1", "50" });
		String expected = "account:ACC1[SECURITY1 150 " + "[[tradeID:1,BUY, account:ACC1, SECURITY1, 100]"
				+ ", [tradeID:2,BUY, account:ACC1, SECURITY1, 50]]]";
		String actual = PositionBookAppService.getUserPositionHoldings(new String[] { "ACC1" });
		Assertions.assertEquals(expected.replaceAll("\\[", "").replaceAll("\\]", ""),
				actual.replace("\n", "").replaceAll("\\[", "").replaceAll("\\]", ""));
	}
	
	/**
	 * Test case to verify same event id is not processed and throw error
	 */
	@Test
	final void testGetUserPositionHoldingsShouldThrowDuplicateEventException() {
		String expected = "Duplicate event request found for event id";
		try {
			PositionBookAppService.processTradeEvent(new String[] { "90", "BUY", "AC90", "SECURITY3", "100" });
			PositionBookAppService.processTradeEvent(new String[] { "90", "BUY", "AC90", "SECURITY3", "100" });
		} catch (InvalidRequestException e) {
			Assertions.assertEquals(true , e.getMessage().contains(expected));
		}
	}
	
	/**
	 * Test case to validate account must exist to fetch account holdings
	 */
	@Test
	final void testGetUserPositionHoldingsShouldThrowException() {
		String expectedErrorMsg = "Account ACC1 not found";
		try {
			PositionBookAppService.getUserPositionHoldings(new String[] { "ACC1" });
		} catch (InvalidRequestException e) {
			Assertions.assertEquals(true, e.getMessage().contains(expectedErrorMsg));
			
		}
	}

	/**
	 * Test case to test scenario to find holdings based on account id and security
	 */
	@Test
	final void testGetSecurityPositionHoldings() {
		PositionBookAppService.processTradeEvent(new String[] { "10", "BUY", "ACC2", "SECURITY1", "100" });
		String expected = "account:ACC2[ SECURITY1 ]100";
		String actual = PositionBookAppService.getSecurityPositionHoldings(new String[] { "ACC2", "SECURITY1" });
		Assertions.assertEquals(expected.replaceAll("\\[", "").replaceAll("\\]", ""),
				actual.replace("\n", "").replaceAll("\\[", "").replaceAll("\\]", ""));
	}
	
	/**
	 * Test case to test find holdings based on account id and security should throw error when account id does not exist
	 */
	@Test
	final void testGetSecurityPositionHoldingsShouldThrowException() {
		String expectedErrorMessage = "Account ACC1 not found";
		try {
			PositionBookAppService.getSecurityPositionHoldings(new String[] { "ACC1", "SECURITY1" });
		} catch (InvalidRequestException e) {
			Assertions.assertEquals(true, e.getMessage().contains(expectedErrorMessage));
			
		}
	}

	/**
	 * Test case for the scenario to buy security
	 */
	@Test
	final void testProcessTradeBuyEvent() {
		String expected = "account:ACC3[SECURITY1 100 tradeID:3,BUY, account:ACC3, SECURITY1, 100]";
		String actual = PositionBookAppService.processTradeEvent(new String[] { "3", "BUY", "ACC3", "SECURITY1", "100" });
		Assertions.assertEquals(expected.replaceAll("\\[", "").replaceAll("\\]", ""), actual.replace("\n", "").replaceAll("\\[", "").replaceAll("\\]", ""));
	}
	
	/**
	 * Test case for the scenario to cancel security
	 */
	@Test
	final void testProcessTradeCancelEvent() {
		String expected = "account:ACC3[SECURITY1 0 tradeID:30,BUY, account:ACC3, SECURITY1, 100]"
				+ ", [tradeID:30,CANCEL, account:ACC3, SECURITY1, 0]";
		PositionBookAppService.processTradeEvent(new String[] { "30", "BUY", "ACC3", "SECURITY1", "100" });
		String actual = PositionBookAppService.processTradeEvent(new String[] { "30", "CANCEL", "ACC3", "SECURITY1", "0" });
		Assertions.assertEquals(expected.replaceAll("\\[", "").replaceAll("\\]", ""), actual.replace("\n", "").replaceAll("\\[", "").replaceAll("\\]", ""));
	}

	/**
	 * Test case for the scenario to sell security
	 */
	@Test
	final void testProcessTradeSellEvent() {
		String expected = "account:ACC3[SECURITY1 0 tradeID:40,BUY, account:ACC3, SECURITY1, 100]"
				+ ", [tradeID:41,SELL, account:ACC3, SECURITY1, 100]";
		PositionBookAppService.processTradeEvent(new String[] { "40", "BUY", "ACC3", "SECURITY1", "100" });
		String actual = PositionBookAppService.processTradeEvent(new String[] { "41", "SELL", "ACC3", "SECURITY1", "100" });
		Assertions.assertEquals(expected.replaceAll("\\[", "").replaceAll("\\]", ""), actual.replace("\n", "").replaceAll("\\[", "").replaceAll("\\]", ""));
	}
	
	/**
	 * Test case for the scenario to verify cancel event will throw error if event id does not exist
	 */
	@Test
	final void testProcessTradeCancelEventThrowException() {
		String expectedErrorMessage = "Can not cancel or sell order which is not placed! for trade id 51";
		try {
			PositionBookAppService.processTradeEvent(new String[] { "51", "SELL", "ACC3", "SECURITY1", "100" });
		} catch (InvalidRequestException e) {
			Assertions.assertEquals(true, e.getMessage().contains(expectedErrorMessage));
			
		}
	}
	
}
