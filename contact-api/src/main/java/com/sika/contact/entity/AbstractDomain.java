package com.sika.contact.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The class Abstract domain.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractDomain implements Serializable {
    /**
     * The Guid.
     */
    @Id
    @Column(name = "ID", nullable = false, updatable = false, columnDefinition = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID id;


    /**
     * The Date created.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated = OffsetDateTime.now();

    /**
     * The Last updated.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated = OffsetDateTime.now();


}
