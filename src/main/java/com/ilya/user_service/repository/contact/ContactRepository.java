package com.ilya.user_service.repository.contact;

import org.springframework.data.repository.CrudRepository;
import com.ilya.user_service.entity.contact.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {
}
