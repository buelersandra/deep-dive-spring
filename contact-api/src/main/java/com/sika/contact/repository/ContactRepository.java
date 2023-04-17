package com.sika.contact.repository;


import com.sika.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends AbstractDomainRepository<Contact>{
    Page<Contact> findAll(Specification<Contact> spec, Pageable pageable);

}
