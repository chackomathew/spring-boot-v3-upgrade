package com.chackomathew.boot;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@DataJpaTest
@ContextConfiguration(initializers = UserRepositoryTests.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private UserRepository userRepository;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            POSTGRES_SQL_CONTAINER.start();
            configurableApplicationContext.getEnvironment()
                    .getSystemProperties()
                    .put("spring.datasource.url", POSTGRES_SQL_CONTAINER.getJdbcUrl());
            configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.username",
                    POSTGRES_SQL_CONTAINER.getUsername());
            configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.password",
                    POSTGRES_SQL_CONTAINER.getPassword());
        }
    }

    @BeforeAll
    public void setup() {
        User user = new User("FirstName", "LastName", false, true);
        userRepository.save(user);
        User user2 = new User("FirstName1", "LastName1", false, false);
        userRepository.save(user2);
    }

    @AfterAll
    public static void stopContainer() {
        POSTGRES_SQL_CONTAINER.stop();
    }

    // Attribute Converter Issue org.springframework.dao.InvalidDataAccessResourceUsageException: JDBC exception executing SQL [select u1_0.id,u1_0.admin,u1_0.deleted,u1_0.first_name,u1_0.last_name from users u1_0 where not(u1_0.deleted)]; SQL [n/a]
    @Test
    public void shouldFindActiveUsers() {
        List<User> users = userRepository.findByDeletedFalse();
        Assertions.assertThat(users).extracting(User::getDeleted).containsOnly(false);
    }

    //  Attribute Converter Issue org.springframework.dao.InvalidDataAccessResourceUsageException: JDBC exception executing SQL [select u1_0.id,u1_0.admin,u1_0.deleted,u1_0.first_name,u1_0.last_name from users u1_0 where u1_0.admin]; SQL [n/a]
    @Test
    public void shouldFindAdminUsers() {
        List<User> users = userRepository.findByAdminTrue();
        Assertions.assertThat(users).extracting(User::getAdmin).containsOnly(true);
    }

    @Test
    public void shouldFindActiveUsersByQuery() {
        List<User> users = userRepository.findByDeletedFalseQuery();
        Assertions.assertThat(users).extracting(User::getDeleted).containsOnly(false);
    }

    @Test
    public void shouldFindAdminUsersByQuery() {
        List<User> users = userRepository.findByAdminTrueQuery();
        Assertions.assertThat(users).extracting(User::getAdmin).containsOnly(true);
    }
}
