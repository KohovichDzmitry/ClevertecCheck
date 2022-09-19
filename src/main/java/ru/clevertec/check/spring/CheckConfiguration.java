package ru.clevertec.check.spring;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.clevertec.check.exceptions.ConnectionException;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "ru.clevertec.check")
@EnableJpaRepositories("ru.clevertec.check.repository")
@EnableTransactionManagement
@EnableConfigurationProperties
public class CheckConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;
    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String sql;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String format;

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driver);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            return dataSource;
        } catch (PropertyVetoException e) {
            throw new ConnectionException("Не удается создать подключение к базе данных ", e);
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("ru.clevertec.check.model");
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.show_sql", sql);
        properties.setProperty("hibernate.format_sql", format);
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaProperties(properties);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurerBean(Environment env) {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yamlFactoryBean = new YamlPropertiesFactoryBean();
        yamlFactoryBean.setResources(determineResources(env));

        PropertiesPropertySource yamlProperties = new PropertiesPropertySource("yml",
                Objects.requireNonNull(yamlFactoryBean.getObject()));

        ((AbstractEnvironment) env).getPropertySources().addLast(yamlProperties);

        propertySourcesPlaceholderConfigurer.setProperties(yamlFactoryBean.getObject());

        return propertySourcesPlaceholderConfigurer;
    }

    private static Resource[] determineResources(Environment env) {
        int numberOfActiveProfiles = env.getActiveProfiles().length;
        ArrayList<Resource> properties = new ArrayList<>(numberOfActiveProfiles);
        properties.add(new ClassPathResource("application.yml"));

        for (String profile : env.getActiveProfiles()) {
            String yamlFile = "application-" + profile + ".yml";
            ClassPathResource props = new ClassPathResource(yamlFile);

            properties.add(props);
        }
        return properties.toArray(new Resource[0]);
    }
}
