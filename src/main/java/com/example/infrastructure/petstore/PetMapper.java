package com.example.infrastructure.petstore;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PetMapper {
    public Pet map(com.example.infrastructure.petstore.model.Pet pet) {
        return Pet.builder()
                .id(pet.getId())
                .name(pet.getName())
                .category(Optional.ofNullable(pet.getCategory())
                        .map(category -> category.getName())
                        .orElse(null))
                .build();
    }


}
