package com.example.controller.dao;

import com.example.infrastructure.petstore.Pet;

import java.util.Optional;

public interface PetDAO {

    Optional<Pet> getPet(final Long petId);
}
