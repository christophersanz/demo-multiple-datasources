package com.example.demomultipledatasources.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableJpaRepositories(entityManagerFactoryRef = "personaEntityManagerFactory",
		transactionManagerRef = "personaTransactionManager",
		basePackages = "com.example.demomultipledatasources.dao.persona")
@Profile("!tc")
public class PostgresConfig {

	@Autowired
	private Environment env;

	public PostgresConfig() {
		super();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean personaEntityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(personaDatasource());
		em.setPackagesToScan("com.example.demomultipledatasources.model.persona");

		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		final HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", env.getProperty("hibernate.db2.database.platform"));
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.ddl.auto"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource personaDatasource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("db2.datasource.url"));
		dataSource.setUsername(env.getProperty("db2.datasource.username"));
		dataSource.setPassword(env.getProperty("db2.datasource.password"));
		dataSource.setDriverClassName(env.getProperty("db2.datasource.driverClassName"));

		return dataSource;
	}

	@Bean(name = "jdbcTemplatepersona")
	public JdbcTemplate jdbcTemplatepersona(){
		return new JdbcTemplate(personaDatasource()); //Dependency Injection只需要呼叫方法即可
	}

	@Bean
	public PlatformTransactionManager personaTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(personaEntityManagerFactory().getObject());

		return transactionManager;
	}

}
