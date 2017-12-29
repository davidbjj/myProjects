package com.cajval.connector.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import org.springframework.util.StringUtils;
import quickfix.field.SecurityType;

public class Stock extends Observable {

	private String symbolId;
	private String settlType;
	private String market;
	protected volatile int hashCode = 0;
	private String securityType;
	private String parentSymbol;
	private String isinCode;
	private String securitySubType = "";
	private Date settlDate;
    private Date settlDate2;
	private String securityId;
	private String tradingSessionID;
	private String newFormatEspecieForSort;
	private String currency;
	private Double contractMultiplier;
	private String securityDesc;
	private String issuer;
	
	public Stock(Stock stock) {
		super();

		if (StringUtils.hasText(stock.getSymbolId())) {
			this.symbolId = stock.getSymbolId();
		}

		if (StringUtils.hasText(stock.getSettlType())) {
			this.settlType = stock.getSettlType();
		}

		if (StringUtils.hasText(stock.getMarket())) {
			this.market = stock.getMarket();
		}

		if (StringUtils.hasText(stock.getSecurityType())) {
			this.securityType = stock.getSecurityType();

		}

		if (StringUtils.hasText(stock.getParentSymbol())) {
			this.parentSymbol = stock.getParentSymbol();
		}

		if (StringUtils.hasText(stock.getIsinCode())) {
			this.isinCode = stock.getIsinCode();
		}

		if (StringUtils.hasText(stock.getSecuritySubType())) {
			this.securitySubType = stock.getSecuritySubType();
		}

		if (stock.getSettlDate() != null) {
			this.settlDate = stock.getSettlDate();
		}

		if (StringUtils.hasText(stock.getTradingSessionID())) {
			this.tradingSessionID = stock.getTradingSessionID();
		}

		if (StringUtils.hasText(stock.getSecurityID())) {
			this.securityId = stock.getSecurityID();
		}

		if (StringUtils.hasText(stock.getCurrency())) {
			this.currency = stock.getCurrency();
		}
		
		if (StringUtils.hasText(stock.getSecurityDesc())) {
			this.securityDesc = stock.getSecurityDesc();
		}
		
	}

	public void setChange() {

		this.setChanged();
	}

	public Stock(String symbolId, String settlType, String market) {
		super();
		this.symbolId = symbolId;
		this.settlType = settlType;
		this.market = market;

	}

	public Stock(String symbolId, String market) {
		super();
		this.symbolId = symbolId;
		this.market = market;
	}

	public Stock(String symbolId, String settlType, String market, String securityType) {
		super();
		this.symbolId = symbolId;
		this.settlType = settlType;
		this.market = market;
		this.securityType = securityType;
	}

	public String getSymbolId() {
		return symbolId;
	}

	public void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}
	
	public String getSettlType() {
		return settlType;
	}
	
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Stock)) {
			return false;
		}
		Stock stock = (Stock) obj;

		if (securityId != null && !securityId.equals("")) {
			return securityId.equals(stock.getSecurityID());
		} else if (settlType != null && !settlType.equals("")) {
			return symbolId.equals(stock.getSymbolId()) && settlType.equals(stock.getSettlType()) && getMarket().equals(stock.getMarket());
		} else {
			if (settlDate != null) {
				return symbolId.equals(stock.getSymbolId()) && settlDate.equals(stock.getSettlDate()) && getMarket().equals(stock.getMarket());
			} else {
				return symbolId.equals(stock.getSymbolId()) && getMarket().equals(stock.getMarket());
			}
		}
	}

	@Override
	public int hashCode() {
		final int multiplier = 23;
		if (hashCode == 0) {
			int code = 133;
			code = multiplier * code + symbolId.hashCode();
			if (securityId != null && !securityId.equals("")) {
				code = multiplier * code + securityId.hashCode();
			} else if (settlType != null && !settlType.equals("")) {
				code = multiplier * code + settlType.hashCode();
			} else {
				if (settlDate != null) {
					code = multiplier * code + settlDate.hashCode();
				}
			}

			code = multiplier * code + market.hashCode();
			// code = multiplier * code + securitySubType.hashCode();
			hashCode = code;
		}
		return hashCode;
	}

	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}

 
	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public String getParentSymbol() {
		return parentSymbol;
	}

	public void setParentSymbol(String parentSymbol) {
		this.parentSymbol = parentSymbol;
	}

	public String getIsinCode() {
		return isinCode;
	}

	public void setIsinCode(String isinCode) {
		this.isinCode = isinCode;
	}

	public String getSecuritySubType() {
		return securitySubType;
	}

	public void setSecuritySubType(String securitySubType) {
		this.securitySubType = securitySubType;
	}

	public Date getSettlDate() {
		return settlDate;
	}

	public void setSettlDate(Date settlDate) {
		this.settlDate = settlDate;
	}

	public String getSecurityID() {
		return securityId;
	}

	public void setSecurityID(String securityID) {
		this.securityId = securityID;
	}

	public String getSecurityTypeDescription() {
		String v = "";
		if (this.getSecurityType().equals(SecurityType.COMMON_STOCK)) {
			if ((this.getSettlType() == null) && (this.getSettlDate() != null)) {
				v = "OrderSenderPanel.plazo";
			} else {
				v = "OrderSenderPanel.commonStock";
			}
		} else if (this.getSecurityType().equals(SecurityType.GENERAL_OBLIGATION_BONDS)) {
			v = "OrderSenderPanel.generalObligationBonds";
		} else if (this.getSecurityType().equals(SecurityType.OPTION)) {
			v = "OrderSenderPanel.option";
		} else if (this.getSecurityType().equals("QS")) {
			v = "OrderSenderPanel.caucion";
		} else if (this.getSecurityType().equals(SecurityType.TERM_LOAN)) {
			v = "OrderSenderPanel.termLoan";
		} else if (this.getSecurityType().equals(SecurityType.SHORT_TERM_LOAN_NOTE)) {
			v = "OrderSenderPanel.sellShortLoan";
		} else if (this.getSecurityType().equals(SecurityType.CERTIFICATE_OF_DEPOSIT)) {
			v = "OrderSenderPanel.commonStock";
		} else if (this.getSecurityType().equals(SecurityType.CORPORATE_BOND)) {
			v = "OrderSenderPanel.commonStock";
		} else if (this.getSecurityType().equals(SecurityType.INDEXED_LINKED)) {
			v = "OrderSenderPanel.indexedLinked";
		}  else if (this.getSecurityType().equals("T")) {
			v = "OrderSenderPanel.plazoPorLotes";
		}
		return v;
	}

	public String getTradingSessionID() {
		return tradingSessionID;
	}

	public void setTradingSessionID(String tradingSessionID) {
		this.tradingSessionID = tradingSessionID;
	}

	public String getNewFormatEspecieForSort() {
		return newFormatEspecieForSort;
	}

	public void setNewFormatEspecieForSort(String newFormatEspecieForSort) {
		this.newFormatEspecieForSort = newFormatEspecieForSort;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getContractMultiplier() {
		return contractMultiplier;
	}

	public void setContractMultiplier(Double contractMultiplier) {
		this.contractMultiplier = contractMultiplier;
	}

	/**
	 * Fecha de liquidacion
	 * @return Date
	 */
	public Date getSettlDate2() {
		return settlDate2;
	}

	public void setSettlDate2(Date settlDate2) {
		this.settlDate2 = settlDate2;
	}
	
	public String getSecurityDesc() {
		return securityDesc;
	}

	public void setSecurityDesc(String securityDesc) {
		this.securityDesc = securityDesc;
	}
	public String getIssuer() {
		return issuer;
	}
		 
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
}
