package com.example.integration.configuration;

import com.example.SpringRestExampleApplication;
import com.example.infrastructure.database.repository.EmployeeRepository;
import com.example.infrastructure.database.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
        classes = {SpringRestExampleApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIntegrationTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    public void after() {
        petRepository.deleteAll();
        employeeRepository.deleteAll();
    }
}
