package com.cajval.connector.utils;

import java.util.Observable;

public  class ObservableMessageState extends Observable {
	
	public void sendMessage(String message) {
		
		setChanged();
		this.notifyObservers(message);
		clearChanged();
	}

}
