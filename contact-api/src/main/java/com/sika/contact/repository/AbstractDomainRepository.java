package com.sika.contact.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The interface Abstract domain repository.
 *
 * @param <T> the type parameter
 */
@NoRepositoryBean
public interface AbstractDomainRepository<T> extends JpaRepository<T, UUID> {}
