package com.ilya.user_service.repository;

import org.springframework.data.repository.CrudRepository;
import com.ilya.user_service.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {
}