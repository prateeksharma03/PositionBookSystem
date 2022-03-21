package jpmorgan.app.dto;

import java.util.Objects;

import jpmorgan.app.constants.Security;
import jpmorgan.app.constants.TradeType;

public class TradeEvent {

	private long id;
	private TradeType tradeType;
	private Security security;
	private int quantity;

	private Account user;
	public TradeEvent(long id, TradeType tradeType, Security security, int quantity, Account user) {
		this.id = id;
		this.tradeType = tradeType;
		this.security = security;
		this.quantity = quantity;
		this.user = user;
	}
	
	public long getId() {
		return id;
	}
	public TradeType getTradeType() {
		return tradeType;
	}
	public Security getSecurity() {
		return security;
	}
	public int getQuantity() {
		return quantity;
	}
	
	public Account getUser(){
		return user;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, security, tradeType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeEvent other = (TradeEvent) obj;
		return id == other.id && security == other.security && tradeType == other.tradeType;
	}

	@Override
	public String toString() {
		return "[tradeID:" + id + "," + tradeType +", "+ user + ", " + security + ", "
				+ quantity + "]\n";
	}
}
