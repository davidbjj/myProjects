package com.cajval.connector.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import quickfix.FieldNotFound;
import quickfix.field.PartyID;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.fix50.NewOrderSingle.NoPartyIDs;
 

public class PartiesContainer{
	private HashMap<String, NoPartyIDs> partiesMap = new HashMap<String, NoPartyIDs>();
	private NoPartyIDs executionFirm;
	private MarketConnectionDescriptor marketConnectionDescriptor;	
	private static final Logger logger = Logger.getLogger(PartiesContainer.class);
	
	public void addParty(String marketKey, NoPartyIDs party){
		
		if("SBA1".equals(marketKey)){
			try {
				setExecutionfirm(party.getString(PartyID.FIELD));
			} catch (FieldNotFound e) {
				logger.error("Error: " + e.getMessage(), e);
			}
		}
		
		partiesMap.put(marketKey, party);		
	}

	private void setExecutionfirm(String partyId) {
		
		NoPartyIDs party = new NoPartyIDs();				
		party.setInt(PartyRole.FIELD, PartyRole.EXECUTING_FIRM);
		party.setChar(PartyIDSource.FIELD, PartyIDSource.PROPRIETARY_CUSTOM_CODE);
		party.setString(PartyID.FIELD, partyId);
		
		
		this.executionFirm = party;
	}
	
	public NoPartyIDs getClearingFirm(String marketKey) {		
		return partiesMap.get(marketKey);		
	}

	public NoPartyIDs getExecutionFirm() {		
		return executionFirm;
	}


	public MarketConnectionDescriptor getMarketConnectionDescriptor() {
		return marketConnectionDescriptor;
	}

	public void setMarketConnectionDescriptor(
			MarketConnectionDescriptor marketConnectionDescriptor) {
		this.marketConnectionDescriptor = marketConnectionDescriptor;
	}
}
