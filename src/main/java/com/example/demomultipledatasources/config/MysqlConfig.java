package com.example.demomultipledatasources.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "comercioEntityManagerFactory",
		transactionManagerRef = "comercioTransactionManager",
		basePackages = "com.example.demomultipledatasources.dao.comercio")
@Profile("!tc")
public class MysqlConfig {
	
	@Autowired
	private Environment env;

	public MysqlConfig() {
		super();
	}

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean comercioEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(comercioDatasource());
		em.setPackagesToScan("com.example.demomultipledatasources.model.comercio");

		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		final HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("hibernate.dialect", env.getProperty("hibernate.spring.database.platform"));
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.ddl.auto"));
		em.setJpaPropertyMap(properties);

		return em;
	}
	
	@Primary
	@Bean
	public DataSource comercioDatasource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		
		return dataSource;
	}

	@Primary
	@Bean(name = "jdbcTemplatecomercio")
	public JdbcTemplate jdbcTemplatecomercio(){
		return new JdbcTemplate(comercioDatasource()); //Dependency Injection只需要呼叫方法即可
	}
	
	@Primary
	@Bean
	public PlatformTransactionManager comercioTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(comercioEntityManagerFactory().getObject());
		
		return transactionManager;
	}

}
