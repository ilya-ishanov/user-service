package com.ilya.user_service.repository.contact;

import org.springframework.data.repository.CrudRepository;
import com.ilya.user_service.entity.contact.ContactPreference;

public interface ContactPreferenceRepository extends CrudRepository<ContactPreference, Long> {
}
