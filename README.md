# PositionBookSystem
# Java version 1.8+
# Gradle version 6.6
# Junit version 5

# Setup steps
1: Clone repository
2: Import into STS as existing gradle porject
3: Run jpmorgan.app.main.PositionBookApplication.main(String[])
4: Can also run Junit test calsses independently

# Assumptions made during the development
1: Input will be space/tab seprated values from console with below format
  # Trade Event input format 
    Input Fromat: id eventType accountId securityType quantity
    
    Input Example 1: 1	 BUY	ACC1	SECURITY1	100
    Output Example 1: ACC1	SECURITY1	100	[id: 1, BUY, ACC1, SECURITY1, 100]
    
    Input Example 2: 2	BUY	ACC1	SECURITY1	50
    Output Example 2: ACC1	SECURITY1	150	[id: 1, BUY, ACC1, SECURITY1, 100] 
					                                [id: 2, BUY, ACC1, SECURITY1, 50]
  # Get Account Holdings
  Input Format: AccountId
  In case we want to fetch account holdings for ACC1 after above trades -
    Input: ACC1
    Output: account:ACC1 SECURITY1 150 
                                    [[tradeID:1,BUY, account:ACC1, SECURITY1, 100]
                                    , [tradeID:2,BUY, account:ACC1, SECURITY1, 50]
                                    ]
   # Get Account Holdings by Security
   Input Format: AccountId SecurityId
   In case we want to fetch account holdings for ACC1 and SECURITY1 after above trades -
   Input: ACC1 SECURITY1
   Output: account:ACC1 SECURITY1 150
   
2: Quantity will be a numeric value
3: In case of Cancle event quantity can be 0
4: If one BUY(or any) event happened with trade id "1" then BUY(or any except CANCLE) event with same trade Id can not be posible
5: Allowed values of trade events - SELL, BUY, CANCEL
6: Allowed values of securities - SECURITY1, SECURITY2, SECURITY3, SECURITY4, SECURITY5;(Can be updated)
7: If User enter invalid trade event or security, should get error
8: Position at any points can be negative in case of short selling
9: In case of cancel evnet request, previous event shoud exist with given event id
10: Trade Id will be a numeric value
