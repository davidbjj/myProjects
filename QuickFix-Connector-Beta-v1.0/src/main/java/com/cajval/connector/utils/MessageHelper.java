package com.cajval.connector.utils;
 
import org.springframework.util.StringUtils;

import quickfix.Message;
import quickfix.field.DeliverToCompID;
import quickfix.field.PartyID;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.fix50.NewOrderSingle.NoPartyIDs;
 

public class MessageHelper {

	
	private Order order;
	private String senderId;
	private String macAddress;
	private String deliverTO;
	private PartiesContainer partiesContainer;

	public MessageHelper(Order order, String senderId, String macAddress, String deliverTO, PartiesContainer partiesContainer){
		this.order = order;
		this.senderId = senderId;
		this.macAddress = macAddress;
		this.deliverTO = deliverTO;
		this.setPartiesContainer(partiesContainer);
		
	}
	
	
	protected void saveSecurityID(Message message) {
		
	}
	protected void saveSecurityIDSource(Message message) {
		
	}	
	protected void saveCurrency(Message message) {
		
	}
		
	protected void saveTraderDate(Message newOrderSingle){		
		
	}
	protected void saveInstrument(Message message) {		
		
	}
	@Deprecated
	protected void saveParties(Message commonOrderMessage, String senderID){
		if (getOrder().isOwnAccount()){

			NoPartyIDs party = new NoPartyIDs();

			party.set(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
			party.set(new PartyRole(PartyRole.AGENT));
			party.set(new PartyID(senderID));
			commonOrderMessage.addGroup(party);

		}
	}
	protected void saveExecutingFirm(Message message) {
		message.addGroup(getPartiesContainer().getExecutionFirm());
	}
	protected void saveClientID(Message message) {		
		message.addGroup(addParty("20111111112", PartyRole.CLIENT_ID));
	}
	protected void saveClearingFirm(Message message) {
		message.addGroup(getPartiesContainer().getClearingFirm(getDeliverTO()));
	}
	
	protected void saveExecutingTrader(Message message) {
		if(StringUtils.hasText(getSenderId())){
			message.addGroup(addParty(getSenderId(), PartyRole.EXECUTING_TRADER));
		}
		
	}
	
	protected void addParties(Message message) throws NoAvailablePartiesException {
		try{
			saveExecutingFirm(message);
			saveClientID(message);
			saveClearingFirm(message);
			saveExecutingTrader(message);
			message.addGroup(addParty(getMacAddress(), PartyRole.DESK_ID));

		}
		catch(Exception e){
			
		}
	}
	
	public void addPartiesWithOutBuild(Message message) throws NoAvailablePartiesException {
		addParties(message);
	}
	


	protected void saveAccountOwn(Message message) {
		/*
		if(getOrder().isOwnAccount()){		
			message.addGroup(addParty(getSenderId(), PartyRole.EXECUTING_TRADER));
		}
		else{
			message.addGroup(addParty(getOrder().getClientAccount(), PartyRole.EXECUTING_TRADER));
		}
		*/
	}
	
	public NoPartyIDs addParty(String partyID, int partyRole){
				
		NoPartyIDs party = new NoPartyIDs();
		
		party.set(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
		party.set(new PartyRole(partyRole)); //identificador de party obtenido de: PartyRole.valor
		party.set(new PartyID(partyID));
		
		return party;		
	}
	
	public static void addHeaderTags(Message message, String value){
		message.getHeader().setField(new DeliverToCompID(value));
	}
	
	public void build(Message newOrderSingle) throws NoAvailablePartiesException{
		if (StringUtils.hasText(getDeliverTO())){
			addHeaderTags(newOrderSingle, getDeliverTO());
		}
		addParties(newOrderSingle);
	}
	
	protected Order getOrder() {
		return order;
	}

	protected String getSenderId() {
		return senderId;
	}
	protected String getMacAddress() {
		return macAddress;
	}
	protected String getDeliverTO() {
		return deliverTO;
	}


	public PartiesContainer getPartiesContainer() {
		return partiesContainer;
	}


	public void setPartiesContainer(PartiesContainer partiesContainer) {
		this.partiesContainer = partiesContainer;
	}
}
