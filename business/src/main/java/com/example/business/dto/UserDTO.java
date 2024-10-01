package com.example.business.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
}
