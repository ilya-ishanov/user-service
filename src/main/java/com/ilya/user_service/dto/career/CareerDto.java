package com.ilya.user_service.dto.career;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerDto {
    private Long id;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String company;
    private String position;
}
