package com.aitho.contocorrente;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
@Configuration
@Profile("test")
public class RepositoryTestConfiguration {

    @Primary
    @Bean
    public DataSource dataSource() {
        // Setup a test data source
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/contocorrente-test");
        dataSource.setUsername("postgres");
        dataSource.setPassword("docker");
        //dataSource.setSchema("contocorrente-test");
        return dataSource;
    }
}
