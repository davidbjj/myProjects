package com.cajval.connector.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import quickfix.FieldNotFound;
import quickfix.fix50.component.Instrument;

public class SecurityListContainer extends Observable {

	private HashMap<String, Map<String, Symbol>> securityListMap = new HashMap<String, Map<String, Symbol>>();

	private HashMap<String, String> securityListRequested = new HashMap<String, String>();

	public HashMap<String, Map<String, Symbol>> getSecurityListMap() {
		return securityListMap;

	}

	public Symbol get(String symbolId, String securityType) {
		HashMap map = (HashMap) securityListMap.get(securityType);
		if (map!=null){
			return (Symbol) map.get(symbolId);
		}
		return null;
	}

	public Set<String> getList(String securityType) {
		HashMap map = (HashMap) securityListMap.get(securityType);
		if (map!=null){
			return map.keySet();
		}
		return null;
	}

	public HashMap<String, String> getSecurityListRequested() {
		return securityListRequested;
	}

	public void setSecurityListRequested(HashMap<String, String> securityListRequested) {
		this.securityListRequested = securityListRequested;
	}

	/**
	 * Notifica a los observadores de este contenedor cuando llega un nuevo instrumento.
	 * </br>
	 * <b>Este metodo debe llamarse en forma explicita cuando se modifica el contenedor</b>
	 * @throws FieldNotFound 
	 */
	public void notifyContainerObservers(Instrument instrument) throws FieldNotFound {
		this.setChanged();
		this.notifyObservers(instrument);
		this.clearChanged();
	}

}
