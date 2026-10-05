package com.ilya.user_service.dto.user;

import lombok.Data;
import com.ilya.user_service.dto.mentorship.CountryDto;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private boolean active;
    private String aboutMe;
    private CountryDto country;
    private String city;
    private Integer experience;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
