package com.example.business.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
}
