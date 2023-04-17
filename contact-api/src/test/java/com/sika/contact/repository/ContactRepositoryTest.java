package com.sika.contact.repository;

import com.sika.contact.domain.response.ContactResponseDTO;
import com.sika.contact.entity.Contact;
import com.sika.contact.service.impl.ContactService;
import com.sika.contact.util.EGender;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDate;
import java.util.UUID;

@ContextConfiguration(classes = {ContactRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.sika.contact.entity"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void test_fetch_all_contact(){
        Contact contact1 = new Contact("Sandra","Konotey","Ghana","Ghanaian",
                UUID.randomUUID().toString(), LocalDate.parse("1995-01-01"), EGender.FEMALE);
        Contact contact2 = new Contact("Sandra","Konotey","Ghana","Ghanaian",
                UUID.randomUUID().toString(), LocalDate.parse("1995-01-01"), EGender.FEMALE);

        contactRepository.save(contact1);
        contactRepository.save(contact2);

        Pageable pageable = PageRequest.of(0,10);
        Page<Contact> page = contactRepository.findAll(pageable);

        Assertions.assertEquals(page.getTotalElements(), 2);
    }

    @Test
    void test_save_contact_with_same_nationality_and_nationalId_throw_DataIntegrityViolationException(){
        String nationalId = UUID.randomUUID().toString();
        Contact contact1 = new Contact("Sandra","Konotey","Ghana","Ghanaian",
                nationalId, LocalDate.parse("1995-01-01"), EGender.FEMALE);
        Contact contact2 = new Contact("Sandra","Konotey","Ghana","Ghanaian",
                nationalId, LocalDate.parse("1995-01-01"), EGender.FEMALE);


        Assertions.assertThrows(DataIntegrityViolationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                contactRepository.save(contact1);
                contactRepository.save(contact2);

                contactRepository.flush();
            }
        });

    }

    @Test
    void test_save_contact_with_same_nationality_and_nationalId_throw_ConstraintViolationException(){
        String nationalId = UUID.randomUUID().toString();
        Contact contact1 = new Contact(null,"Konotey","Ghana","Ghanaian",
                nationalId, LocalDate.parse("1995-01-01"), EGender.FEMALE);



        Assertions.assertThrows(ConstraintViolationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                contactRepository.save(contact1);

                contactRepository.flush();
            }
        });

    }

    @Test
    void test_fetch_all_contact_by_word(){
        Contact contact1 = new Contact("Sandra","Konotey","Ghana","Ghanaian",
                UUID.randomUUID().toString(), LocalDate.parse("1995-01-01"), EGender.FEMALE);
        Contact contact2 = new Contact("Abena","Konotey","Ghana","Ghanaian",
                UUID.randomUUID().toString(), LocalDate.parse("1995-01-01"), EGender.FEMALE);

        contactRepository.save(contact1);
        contactRepository.save(contact2);

        Pageable pageable = PageRequest.of(0,10);
        Page<Contact> page = contactRepository.findAll(ContactService.textInAllColumns("Abena"), pageable);

        Assertions.assertEquals(page.getTotalElements(), 1);
        ContactResponseDTO responseDTO = page.stream().findFirst().get().responseDTO();

        Assertions.assertEquals(responseDTO.getLastName(),contact2.getLastName());
        Assertions.assertEquals(responseDTO.getFirstName(),contact2.getFirstName());

        Page<Contact> page2 = contactRepository.findAll(ContactService.textInAllColumns("Konotey"), pageable);
        Assertions.assertEquals(page2.getTotalElements(), 2);

    }



}
