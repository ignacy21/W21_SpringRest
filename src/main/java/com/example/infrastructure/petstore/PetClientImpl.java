package com.example.infrastructure.petstore;

import com.example.controller.dao.PetDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PetClientImpl implements PetDAO {

    private final WebClient webClient;

    @Override
    public Optional<Pet> getPet(Long petId) {

        try {
            Pet result = webClient
                    .get()
                    .uri("/pet/" + petId)
                    .retrieve()
                    .bodyToMono(Pet.class)
                    .block();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
