package com.cajval.connector;

import javax.management.JMException;

import org.quickfixj.jmx.JmxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

@Component
public class SenderApp {

	public static SenderApp senderApp;
	private boolean initiatorStarted = false;
	private SocketInitiator initiator = null;
	private static final Logger log = LoggerFactory.getLogger(SenderApp.class);

	public void SenderAppInit() throws ConfigError, InterruptedException, JMException {
		SessionSettings settings = new SessionSettings("./config/sender.cfg");
		Application myApp = new FIXSender();
		FileStoreFactory fileStoreFactory = new FileStoreFactory(settings);
		ScreenLogFactory screenLogFactory = new ScreenLogFactory(settings);
		DefaultMessageFactory msgFactory = new DefaultMessageFactory();
		initiator = new SocketInitiator(myApp, fileStoreFactory, settings, screenLogFactory, msgFactory);

		JmxExporter exporter = new JmxExporter();
		exporter.register(initiator);

	}

	public synchronized void logon() {
		if (!initiatorStarted) {
			try {
				initiator.start();
				initiatorStarted = true;
			} catch (Exception e) {
				log.error("Logon failed", e);
			}
		} else {
			for (SessionID sessionId : initiator.getSessions()) {
				Session.lookupSession(sessionId).logon();
			}
		}
	}

	public void logout() {
		for (SessionID sessionId : initiator.getSessions()) {
			Session.lookupSession(sessionId).logout("user requested");
		}
	}
}