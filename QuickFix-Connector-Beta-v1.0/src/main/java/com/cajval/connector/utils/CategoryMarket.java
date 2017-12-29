package com.cajval.connector.utils;

public class CategoryMarket {

	public Double getOptions() {
		return options;
	}

	public void setOptions(Double options) {
		this.options = options;
	}

	public Double getBonds() {
		return bonds;
	}

	public void setBonds(Double bonds) {
		this.bonds = bonds;
	}

	public Double getRepo() {
		return repo;
	}

	public void setRepo(Double repo) {
		this.repo = repo;
	}

	public Double getEquity() {
		return equity;
	}

	public void setEquity(Double equity) {
		this.equity = equity;
	}
	
	private Double equity;

	private Double bonds;
	
	private Double repo;
	
	private Double options;

}
