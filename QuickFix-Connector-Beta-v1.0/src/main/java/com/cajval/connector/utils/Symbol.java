package com.cajval.connector.utils;

import java.io.Serializable;

import ar.com.sba.cajval.fixserver.domain.PriceLimits;

public class Symbol implements Serializable {

	private static final long serialVersionUID = -3187592172622056914L;
	private String symbolId;
	private String securityType;
	private Integer product;
	private String description;
	private String maturityDate;
	private PriceLimits priceLimits;
	private String currency;
	private String CFICode;
	private String securityExchange;
	
	public String getSymbolId() {
		return symbolId;
	}

	public void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public Integer getProduct() {
		return product;
	}

	public void setProduct(Integer product) {
		this.product = product;
	}

	public Symbol(String symbolId, String securityType, Integer product) {
		super();
		this.symbolId = symbolId;
		this.securityType = securityType;
		this.product = product;
	}

	public Symbol(String symbolId, String securityType, Integer product, String description) {
		super();
		this.symbolId = symbolId;
		this.securityType = securityType;
		this.product = product;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Symbol [symbolId=" + symbolId + ", securityType=" + securityType + ", product=" + product + "]";
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public PriceLimits getPriceLimits() {
		return priceLimits;
	}

	public void setPriceLimits(PriceLimits priceLimits) {
		this.priceLimits = priceLimits;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCFICode() {
		return CFICode;
	}

	public void setCFICode(String cFICode) {
		CFICode = cFICode;
	}

	public String getSecurityExchange() {
		return securityExchange;
	}

	public void setSecurityExchange(String securityExchange) {
		this.securityExchange = securityExchange;
	}
	
}
