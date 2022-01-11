package com.gcn.heco.database.config;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
	
	static Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

	@Value("${db.driver}")
	private String dbDriver;

	@Value("${db.password}")
	private String dbPassword;

	@Value("${db.url}")
	private String dbUrl;

	@Value("${db.username}")
	private String dbUserName;

	@Value("${hibernate.dialect}")
	private String hibDialect;

	@Value("${hibernate.show_sql}")
	private String hibShowSql;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hibHbm2DdlAuto;

	@Value("${entitymanager.packagesToScan}")
	private String entityPackagesToScan;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		try {
			dataSource.setDriverClassName(dbDriver);
			dataSource.setUrl(dbUrl);
			dataSource.setUsername(dbUserName);
			dataSource.setPassword(dbPassword);
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		try {
			sessionFactoryBean.setDataSource(dataSource());
			sessionFactoryBean.setPackagesToScan(entityPackagesToScan);
			Properties hibernateProperties = new Properties();
			hibernateProperties.put("hibernate.dialect", hibDialect);
			hibernateProperties.put("hibernate.show_sql", hibShowSql);
			hibernateProperties.put("hibernate.hbm2ddl.auto", hibHbm2DdlAuto);
			sessionFactoryBean.setHibernateProperties(hibernateProperties);

		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		return sessionFactoryBean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = 
				new HibernateTransactionManager();
		try {
			transactionManager.setSessionFactory(sessionFactory().getObject());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		return transactionManager;
	}

}

