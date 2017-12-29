package com.cajval.connector;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CountDownLatch;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import infinispan.autoconfigure.embedded.InfinispanCacheConfigurer;
import infinispan.autoconfigure.embedded.InfinispanGlobalConfigurer;

@SpringBootApplication
public class ConnectorApplication implements CommandLineRunner {

	@Autowired
	private SenderApp senderApp;

	private static final String CACHE_NAME = "CacheFix";
	private static final CountDownLatch shutdownLatch = new CountDownLatch(1);
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Bean
	public InfinispanGlobalConfigurer globalConfiguration() {
		logger.info("Defining Global Configuration");
		return () -> GlobalConfigurationBuilder.defaultClusteredBuilder().globalJmxStatistics()
				.allowDuplicateDomains(true).build();
	}

	 
	@Bean
	public InfinispanCacheConfigurer cacheConfigurer() {
		logger.info("Defining {} configuration", CACHE_NAME);
		return manager -> {
			Configuration ispnConfig = new ConfigurationBuilder().clustering().cacheMode(CacheMode.DIST_SYNC).build();

			manager.defineConfiguration(CACHE_NAME, ispnConfig);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ConnectorApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		senderApp.SenderAppInit();
		senderApp.logon();
		shutdownLatch.await();
	}

	public void stop() {
		shutdownLatch.countDown();
	}
}
