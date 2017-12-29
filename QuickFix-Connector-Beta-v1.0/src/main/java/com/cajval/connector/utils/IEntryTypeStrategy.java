package com.cajval.connector.utils;

import quickfix.fix50.MarketDataRequest;
import quickfix.fix50.component.Instrument;

/**
 * Created with IntelliJ IDEA.
 * User: fdv
 * Date: 27/03/15
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IEntryTypeStrategy {

    public void defineEntryTypes(Instrument instrument, MarketDataRequest marketDataRequest);
}
