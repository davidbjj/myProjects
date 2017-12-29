package com.cajval.connector;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cajval.connector.utils.ExchangeClock;

import com.cajval.connector.utils.MessageHelper;
import com.cajval.connector.utils.ObservableLogon;
import com.cajval.connector.utils.ObservableMessageState;
import com.cajval.connector.utils.SecurityListContainer;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.AggregatedBook;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateType;
import quickfix.field.MarketDepth;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.Product;
import quickfix.field.SecurityListRequestType;
import quickfix.field.SecurityReqID;
import quickfix.field.SecuritySubType;
import quickfix.field.SecurityType;
import quickfix.field.SendingTime;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.Username;
import quickfix.fix50.SecurityListRequest;
import quickfix.fix50.MarketDataRequest.NoMDEntryTypes;
import quickfix.fix50.component.InstrmtMDReqGrp;
import quickfix.fix50.component.Instrument;
import quickfix.fix50.MarketDataRequest;
import quickfix.fixt11.Logon;

@Component
public class FIXSender implements Application {

	private int sum;
	private ExchangeClock exchangeClock;
	private String connectionStatus;
	private int sentSecurityList = 0;
	private static LocalDateTime serverSendingTime;

	private static final Logger logger = Logger.getLogger(FIXSender.class);
	private SecurityListContainer securityListContainer = new SecurityListContainer();

	private HashMap<String, MarketDataRequest> marketSubscribe = new HashMap<String, MarketDataRequest>();
	private ObservableLogon observableLogon = new ObservableLogon();
	private ObservableMessageState observableMessageState = new ObservableMessageState();

	@Override
	public void fromAdmin(quickfix.Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		if (message.getHeader().getString(MsgType.FIELD).equals(MsgType.LOGON)) {
			this.getObservableMessageState().sendMessage("");
			if (message.getHeader().isSetField(SendingTime.FIELD)) {
				this.setServerSendingTime(calculateDiffDate(message));
			}
		} else if (message.getHeader().getString(MsgType.FIELD).equals(MsgType.LOGOUT)) {
			try {
				String msg = message.getString(Text.FIELD);

				this.connectionStatus = msg;
				if (msg.contains("SendingTime accuracy problem")) {
					this.connectionStatus = "TimeAccuracyError";
				}
			} catch (FieldNotFound e) {
				this.connectionStatus = FixFinalConstants.DEFAULT_CONNECTION_ERROR;
			}
			observableLogon.onError(connectionStatus);
		}

		if (message.getHeader().getString(MsgType.FIELD).equals(MsgType.HEARTBEAT)) {
			calculateDiffDate(message);
			observableLogon.onResetWarning();
		}

	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
	}

	@Override
	public void onCreate(SessionID message) {
	}

	@Override
	public void onLogon(SessionID sessionID) {
		MITMarket();
		onLogout(sessionID);
	}

	@Override
	public void onLogout(SessionID sessionID) {
		Session.lookupSession(sessionID).logout();
		observableLogon.logoff(sessionID);
		System.exit(0);
	}

	@Override
	public void toAdmin(Message message, SessionID sessionID) {
		try {
			if (message.getHeader().getString(MsgType.FIELD).equals(MsgType.LOGON)) {

				((Logon) message).setField(new Username(getUser(FixFinalConstants.USER)));
				((Logon) message).setField(new Password(getUser(FixFinalConstants.PASS)));
			}
			if (message.getHeader().getString(MsgType.FIELD).equals(MsgType.TEST_REQUEST)) {
				observableLogon.onWarning();
			}

		} catch (FieldNotFound e) {
			logger.error("Error: " + e.getMessage(), e);
		}
	}

	@Override
	public void toApp(Message msg, SessionID sessionId) throws DoNotSend {
	}

	public void sendMessage(SecurityListRequest securityListRequest, String senderID, String targetID)
			throws SessionNotFound {
		Session.sendToTarget(securityListRequest, this.getUser(FixFinalConstants.USERFIX),
				this.getUser(FixFinalConstants.SERVERNAME));
	}

	public void sendDataMarketRequest() throws SessionNotFound, FieldNotFound {
		this.sendDataMarketRequest("");
	}

	public void sendDataMarketRequest(String deliverTO) throws SessionNotFound, FieldNotFound {

		MarketDataRequest marketDataRequest = new MarketDataRequest();

		if (StringUtils.hasText(deliverTO)) {
			MessageHelper.addHeaderTags(marketDataRequest, deliverTO);
		}

		String mdReqID = getClordId();
		marketDataRequest.set(new MDReqID(mdReqID));

		marketDataRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT));
		defineUpdateType(marketDataRequest);

		marketDataRequest.set(new MarketDepth(10));
		marketDataRequest.set(new AggregatedBook(true));

		InstrmtMDReqGrp instrumentGroup = new InstrmtMDReqGrp();
		InstrmtMDReqGrp.NoRelatedSym noRelatedSym = new InstrmtMDReqGrp.NoRelatedSym();
		Instrument instrument = new Instrument();
		instrument.set(new Symbol("[N/A]"));
		noRelatedSym.set(instrument);
		instrumentGroup.addGroup(noRelatedSym);
		marketDataRequest.set(instrumentGroup);
		defineEntryTypes(instrument, marketDataRequest);
		Session.sendToTarget(marketDataRequest, this.getUser(FixFinalConstants.USER),
				this.getUser(FixFinalConstants.SERVERNAME));
		this.getMarketSubscribe().put(mdReqID, marketDataRequest);
	}

	protected void defineUpdateType(MarketDataRequest marketDataRequest) {
		marketDataRequest.set(new MDUpdateType(MDUpdateType.INCREMENTAL_REFRESH));
	}

	public void MITMarket() {
		try {
			 sendDataMarketRequest(FixFinalConstants.MARKET_MIT);
		} catch (SessionNotFound | FieldNotFound e) {
			e.printStackTrace();
		}
	}

	protected void defineEntryTypes(Instrument instrument, MarketDataRequest marketDataRequest) {
		NoMDEntryTypes noMDEntryTypes = new NoMDEntryTypes();

		MDEntryType mdEntryType_bid = new MDEntryType(MDEntryType.BID);
		noMDEntryTypes.set(mdEntryType_bid);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_offer = new MDEntryType(MDEntryType.OFFER);
		noMDEntryTypes.set(mdEntryType_offer);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_trade = new MDEntryType(MDEntryType.TRADE);
		noMDEntryTypes.set(mdEntryType_trade);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_volume = new MDEntryType(MDEntryType.TRADE_VOLUME);
		noMDEntryTypes.set(mdEntryType_volume);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_OPENING_PRICE = new MDEntryType(MDEntryType.OPENING_PRICE);
		noMDEntryTypes.set(mdEntryType_OPENING_PRICE);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_TRADING_SESSION_HIGH_PRICE = new MDEntryType(MDEntryType.TRADING_SESSION_HIGH_PRICE);
		noMDEntryTypes.set(mdEntryType_TRADING_SESSION_HIGH_PRICE);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_TRADING_SESSION_LOW_PRICE = new MDEntryType(MDEntryType.TRADING_SESSION_LOW_PRICE);
		noMDEntryTypes.set(mdEntryType_TRADING_SESSION_LOW_PRICE);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_IMBALANCE = new MDEntryType(MDEntryType.IMBALANCE);
		noMDEntryTypes.set(mdEntryType_IMBALANCE);
		marketDataRequest.addGroup(noMDEntryTypes);

		MDEntryType mdEntryType_CLOSING_PRICE = new MDEntryType(MDEntryType.CLOSING_PRICE);
		noMDEntryTypes.set(mdEntryType_CLOSING_PRICE);
		marketDataRequest.addGroup(noMDEntryTypes);
		MDEntryType mdEntryType_INDEX_VALUE = new MDEntryType(MDEntryType.INDEX_VALUE);
		noMDEntryTypes.set(mdEntryType_INDEX_VALUE);
		marketDataRequest.addGroup(noMDEntryTypes);
	}

	public void sendSecurityListRequest(String type, String deliverTo) throws SessionNotFound {
		SecurityListRequest securityListRequest = new SecurityListRequest();
		if (StringUtils.hasText(deliverTo)) {
			MessageHelper.addHeaderTags(securityListRequest, deliverTo);
		}

		securityListRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT));

		if (type == null) {
			securityListRequest.set(new SecurityListRequestType(SecurityListRequestType.ALL_SECURITIES));

		} else {
			if (type.equals(FixFinalConstants.INDICES)) {
				securityListRequest.set(new SecurityListRequestType(SecurityListRequestType.PRODUCT));
				securityListRequest.set(new Product(Product.INDEX));

			} else {
				securityListRequest
						.set(new SecurityListRequestType(SecurityListRequestType.SECURITYTYPE_AND_OR_CFICODE));
				if (type.equals(FixFinalConstants.GENERAL_OBLIGATION_BONDS)) {
					securityListRequest.set(new SecurityType(SecurityType.GENERAL_OBLIGATION_BONDS));

				} else if (type.equals(FixFinalConstants.MERVAL) || type.equals(FixFinalConstants.GENERAL)) {
					securityListRequest.set(new SecurityType(SecurityType.COMMON_STOCK));
					securityListRequest.set(new SecuritySubType(type));

				} else if (type.equals(FixFinalConstants.CERTIFICATE_OF_DEPOSIT)) {
					securityListRequest.set(new SecurityType(SecurityType.CERTIFICATE_OF_DEPOSIT));

				} else if (type.equals(FixFinalConstants.CORPORATE_BONDS)) {
					securityListRequest.set(new SecurityType(SecurityType.CORPORATE_BOND));

				} else if (type.equals(FixFinalConstants.OPTION)) {
					securityListRequest.set(new SecurityType(SecurityType.OPTION));
					securityListRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));

				} else if (type.equals(FixFinalConstants.CAUTION)) {
					securityListRequest.set(new SecurityType(FixFinalConstants.CAUTION));
					securityListRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));

				} else if (type.equals(SecurityType.TERM_LOAN)) {
					securityListRequest.set(new SecurityType(SecurityType.TERM_LOAN));
					securityListRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));

				} else if (type.equals(FixFinalConstants.PLAZO)) {
					securityListRequest.set(new SecurityType(SecurityType.COMMON_STOCK));
					securityListRequest.set(new SecuritySubType(type));

				} else if (type.equals(FixFinalConstants.PLAZO_POR_LOTES)) {
					securityListRequest.set(new SecurityType(FixFinalConstants.PLAZO_POR_LOTES));

				} else if (type.equals(SecurityType.INDEXED_LINKED)) {
					securityListRequest.set(new SecurityType(SecurityType.INDEXED_LINKED));
					securityListRequest.set(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));
				}
			}
		}

		String secReqID = getClordId();
		securityListRequest.set(new SecurityReqID(secReqID));
		this.securityListContainer.getSecurityListRequested().put(secReqID, type);
		this.sendMessage(securityListRequest, this.getUser(FixFinalConstants.USERFIX),
				this.getUser(FixFinalConstants.SERVERNAME));
	}

	private LocalDateTime calculateDiffDate(quickfix.Message message) throws FieldNotFound {

		SendingTime sendingTimeField = new SendingTime();
		LocalDateTime d = message.getHeader().getField(sendingTimeField).getValue();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime = now.format(formatter);
		String serverTime = d.format(formatter);
		return d;
	}

	public String getClordId() {
		sum++;
		String clordID = this.getUser(FixFinalConstants.USERFIX) + "_" + sum + "_"
				+ Calendar.getInstance().getTimeInMillis();

		if (sum == 1000) {
			sum = 0;
		}
		return clordID;
	}

	public String getUser(String arg) {
		String users = "";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		try {
			prop.load(classLoader.getResourceAsStream("users.properties"));
			if (arg.equals(FixFinalConstants.USER))
				users = prop.getProperty("user");
			if (arg.equals(FixFinalConstants.PASS))
				users = prop.getProperty("password");
			if (arg.equals(FixFinalConstants.USERFIX))
				users = prop.getProperty("userfix");
			if (arg.equals(FixFinalConstants.SERVERNAME))
				users = prop.getProperty("servername");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users;
	}

	/*
	 * ---------------- GETTER/SETTERS
	 * ---------------------------------------------------------------
	 */

	public HashMap<String, MarketDataRequest> getMarketSubscribe() {
		return marketSubscribe;
	}

	public void setMarketSubscribe(HashMap<String, MarketDataRequest> marketSubscribe) {
		this.marketSubscribe = marketSubscribe;
	}

	public ObservableLogon getObservableLogon() {
		return observableLogon;
	}

	public void setObservableLogon(ObservableLogon observableLogon) {
		this.observableLogon = observableLogon;
	}

	public ObservableMessageState getObservableMessageState() {
		return observableMessageState;
	}

	public void setObservableMessageState(ObservableMessageState observableMessageState) {
		this.observableMessageState = observableMessageState;
	}

	public ExchangeClock getExchangeClock() {
		return exchangeClock;
	}

	public void setExchangeClock(ExchangeClock exchangeClock) {
		this.exchangeClock = exchangeClock;
	}

	public int getSentSecurityList() {
		return sentSecurityList;
	}

	public void setSentSecurityList(int sentSecurityList) {
		this.sentSecurityList = sentSecurityList;
	}

	public static LocalDateTime getServerSendingTime() {
		return serverSendingTime;
	}

	public void setServerSendingTime(LocalDateTime localDateTime) {
		this.serverSendingTime = localDateTime;
	}

}
