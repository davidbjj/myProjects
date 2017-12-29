package com.cajval.connector;

import static org.junit.Assert.assertEquals;

import javax.management.JMException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import quickfix.ConfigError;
import quickfix.SessionSettings;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectorApplicationTests {

	@Autowired
	private SenderApp senderApp;
	
	@Test
	public void contextLoads() throws ConfigError, InterruptedException, JMException {
		SessionSettings settings = new SessionSettings("./config/sender.cfg");
		String key1 = "initiator";
		String Key2 = settings.getDefaultProperties().getProperty("ConnectionType");
		assertEquals(key1, Key2); 
	}
	
	@Test
	public void AppInit() throws ConfigError {}

	
	
	public SenderApp getSenderApp() {
		return senderApp;
	}

	public void setSenderApp(SenderApp senderApp) {
		this.senderApp = senderApp;
	}
		

}
