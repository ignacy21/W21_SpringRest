package com.example.integration.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public class PersistenceContainerTestConfiguration {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String POSTGRESQL = "postgresql";
    public static final String POSTGRES_CONTAINER = "postgres:16.0";

    @Bean
    @Qualifier(POSTGRESQL)
    PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRES_CONTAINER)
                .withUsername(USERNAME)
                .withPassword(PASSWORD);
        container.start();
        return container;
    }

    @Bean
    DataSource dataSource(final PostgreSQLContainer<?> postgreSQLContainer) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }
}
