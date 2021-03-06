package com.activemq.dem.service.activemq.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.activemq.store.journal.JournalPersistenceAdapterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

@EnableJms
@Configuration
@EnableScheduling
public class JmsConfig
{

	@Value("${org.apache.activemq.driverClass}")
	private String driverClass;

	@Value("${org.apache.activemq.jdbcUrl}")
	private String jdbcUrl;

	@Value("${org.apache.activemq.user}")
	private String user;

	@Value("${org.apache.activemq.password}")
	private String password;

	@Value("${pp.jms.min}")
	private int min;

	@Value("${pp.jms.max}")
	private int max;

	private static final String MASTER_URL = "vm://localhost?broker.persistent=true";

	private static final String BROKER_NAME = "broker";

	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(MASTER_URL);
		factory.setTrustedPackages(Arrays.asList("com.activemq.dem"));
		ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
		prefetchPolicy.setAll(1);
		factory.setPrefetchPolicy(prefetchPolicy);
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(factory);
		pooledConnectionFactory.setReconnectOnException(true);
		return pooledConnectionFactory;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory(DefaultJmsListenerContainerFactoryConfigurer configurer)
	{
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrency(min + "-" + max);
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory());
		// You could still override some of Boot's default if necessary.
		return factory;
	}


	//Persistence Oracle
	@Bean
	public PersistenceAdapter jdbcPersistenceAdapter() throws SQLException, IOException, PropertyVetoException
	{
		JDBCPersistenceAdapter jdbcPersistenceAdapter = new JDBCPersistenceAdapter();
		jdbcPersistenceAdapter.setDataSource(oracleDs());
		jdbcPersistenceAdapter.setBrokerName(BROKER_NAME);
		jdbcPersistenceAdapter.setDataDirectory("${activemq.data}");
		jdbcPersistenceAdapter.setUseLock(false);
		return jdbcPersistenceAdapter;
	}

	@Bean
	public JournalPersistenceAdapterFactory journalPersistenceAdapterFactory() throws PropertyVetoException
	{
		JournalPersistenceAdapterFactory persistenceAdapterFactory = new JournalPersistenceAdapterFactory();
		persistenceAdapterFactory.setJournalLogFiles(5);
		persistenceAdapterFactory.setDataSource(oracleDs());
		persistenceAdapterFactory.setUseDatabaseLock(false);
		return persistenceAdapterFactory;
	}

	@Bean(name = "oracleDs")
	public ComboPooledDataSource oracleDs() throws PropertyVetoException
	{
		ComboPooledDataSource oracleDataSource = new ComboPooledDataSource();
		oracleDataSource.setDriverClass(driverClass);
		oracleDataSource.setJdbcUrl(jdbcUrl);
		oracleDataSource.setUser(user);
		oracleDataSource.setPassword(password);
		oracleDataSource.setMinPoolSize(1);
		oracleDataSource.setMaxPoolSize(200);
		oracleDataSource.setInitialPoolSize(2);
		oracleDataSource.setAcquireIncrement(1);
		oracleDataSource.setMaxIdleTime(600);
		oracleDataSource.setMaxStatements(2000);
		oracleDataSource.setCheckoutTimeout(5000);
		oracleDataSource.setIdleConnectionTestPeriod(60);
		return oracleDataSource;
	}

	@Bean
	public BrokerService brokerService() throws Exception
	{
		BrokerService brokerService = new BrokerService();
		brokerService.setPersistent(true);
		brokerService.setPersistenceAdapter(jdbcPersistenceAdapter());
		brokerService.setPersistenceFactory(journalPersistenceAdapterFactory());
		brokerService.setUseJmx(true);
		brokerService.setBrokerName(BROKER_NAME);
		brokerService.addConnector(MASTER_URL);
		return brokerService;
	}
}
