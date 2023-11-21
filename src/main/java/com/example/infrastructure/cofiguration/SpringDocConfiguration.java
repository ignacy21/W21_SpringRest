package com.example.infrastructure.cofiguration;

import com.example.SpringRestExampleApplication;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi groupedOnepApi() {
        return GroupedOpenApi.builder()
                .group("deafult")
                .pathsToMatch("/**")
                .packagesToScan(SpringRestExampleApplication.class.getPackageName())
                .build();
    }

    @Bean
    public OpenAPI springDocOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Employee application")
                        .contact(contact())
                        .version("1.0"));
    }

    private Contact contact() {
        return new Contact()
                .name("Zajavka")
                .url("https://zajavka.pl")
                .email("kontakt@zajavka.pl");
    }

}
