package com.example.infrastructure.petstore;

import com.example.controller.dao.PetDAO;
import com.example.infrastructure.petstore.api.PetApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PetClientImpl implements PetDAO {

    private final PetApi petApi;
    private final PetMapper petMapper;

    @Override
    public Optional<Pet> getPet(Long petId) {

        try {
            return Optional.ofNullable(petApi.getPetById(petId).block())
                    .map(petMapper::map);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
