package com.cajval.connector.utils;

import java.util.HashMap;

public class MarketConnectionDescriptor {
	private String rofexID;
	private String xrorxID;
	private String mendozaID;
	private String cordobaID;
	private String maeID;
	private String matbaID;
	private String maeMatbaMarketDataID;
	private String mitID;
	private CategoryMarket categoryMarket;
	
	private HashMap<String,String> marketNameMap = new HashMap<String, String>();
	
	
	public String getRofexID() {
		return rofexID;
	}


	public void setRofexID(String rofexID) {
		this.rofexID = rofexID;
	}

	public String getXrorxID() {
		return xrorxID;
	}


	public void setXrorxID(String xrorxID) {
		this.xrorxID = xrorxID;
	}


	public String getMendozaID() {
		return mendozaID;
	}


	public void setMendozaID(String mendozaID) {
		this.mendozaID = mendozaID;
	}


	public String getCordobaID() {
		return cordobaID;
	}


	public void setCordobaID(String cordobaID) {
		this.cordobaID = cordobaID;
	}


	public String getMaeID() {
		return maeID;
	}


	public void setMaeID(String maeID) {
		this.maeID = maeID;
	}


	public String getMatbaID() {
		return matbaID;
	}


	public void setMatbaID(String matbaID) {
		this.matbaID = matbaID;
	}


	public String getMaeMatbaMarketDataID() {
		return maeMatbaMarketDataID;
	}


	public void setMaeMatbaMarketDataID(String maeMatbaMarketDataID) {
		this.maeMatbaMarketDataID = maeMatbaMarketDataID;
	}


	public HashMap<String, String> getMarketNameMap() {
		return marketNameMap;
	}


	public void setMarketNameMap(HashMap<String, String> marketNameMap) {
		this.marketNameMap = marketNameMap;
	}

	public String getMitID() {
		return mitID;
	}

	public void setMitID(String mitID) {
		this.mitID = mitID;
	}

    public boolean marketExists(String marketId){
        for(String id : this.getMarketNameMap().keySet()){
            if(marketId.equals(id)){
                return true;
            }

        }
        return false;
    }

	public CategoryMarket getCategoryMarket() {
		return categoryMarket;
	}


	public void setCategoryMarket(CategoryMarket categoryMarket) {
		this.categoryMarket = categoryMarket;
	}
	
}
