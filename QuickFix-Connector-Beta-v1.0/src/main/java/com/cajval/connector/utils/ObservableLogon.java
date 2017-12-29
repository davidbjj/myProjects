package com.cajval.connector.utils;

import java.util.HashSet;
import java.util.Observable;

import quickfix.SessionID;

public  class ObservableLogon extends Observable {
	private HashSet<SessionID> set = new HashSet<SessionID>();

	public void logon(SessionID sessionID) {
		set.add(sessionID);
		setChanged();
		this.notifyObservers("ON");
		clearChanged();
	}

	public void logoff(SessionID sessionID) {
		set.remove(sessionID);
		setChanged();
		this.notifyObservers("OFF");
		clearChanged();
	}
	
	public void logoff(SessionID sessionID, String tooltip) {
		set.remove(sessionID);
		setChanged();
		this.notifyObservers("OFF," + tooltip);
		clearChanged();
	}
	
	public void onError(String description){
		setChanged();
		this.notifyObservers(description);
		clearChanged();			
	}

	public void onWarning() {
		setChanged();
		this.notifyObservers("WARN");
		clearChanged();
	}

	public void onResetWarning() {
		setChanged();
		this.notifyObservers("RESET_WARN");
		clearChanged();
	}
	
}
