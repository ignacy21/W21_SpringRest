package com.example.controller.dto;

import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {

    private Integer petId;
    private Long petStoreId;
    private String name;
    private String category;

}
