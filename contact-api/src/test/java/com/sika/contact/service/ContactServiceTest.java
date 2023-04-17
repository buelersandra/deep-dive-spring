package com.sika.contact.service;

import com.sika.contact.common.ApiPaths;
import com.sika.contact.domain.request.ContactRequestDTO;
import com.sika.contact.domain.response.BaseResponseDTO;
import com.sika.contact.domain.response.ContactResponseDTO;
import com.sika.contact.entity.Contact;
import com.sika.contact.repository.ContactRepository;
import com.sika.contact.service.impl.ContactService;
import com.sika.contact.util.EGender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource({"classpath:application.properties"})
public class ContactServiceTest {


    @Autowired
    ContactService mockContactService;

    @MockBean
    ContactRepository contactRepository;


    @Test
    void when_create_return_success(){

        Contact contact = mock(Contact.class);
        Mockito.when(contactRepository.save(any())).thenReturn(contact);

        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName("Sandra")
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();


        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.create(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(response.getBody().getMessage(),ApiPaths.SUCCESS_MSG);
    }


    @Test
    void when_list_of_contacts_return_success(){
        Mockito.when(contactRepository.findAll((Pageable) any())).thenReturn(mock(Page.class));

        ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> response = mockContactService.list(0,10, Sort.Direction.DESC,"dateCreated");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals(ApiPaths.SUCCESS_MSG, response.getBody().getMessage());

    }

    @Test
    void when_search_contacts_by_word_return_success(){
        Mockito.when(contactRepository.findAll((Specification<Contact>) any(),(Pageable) any())).thenReturn(mock(Page.class));

        ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> response = mockContactService.search("test",0,10, Sort.Direction.DESC,"dateCreated");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    void when_get_contact_return_success(){

        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.of(mock(Contact.class)));

        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.get(UUID.randomUUID().toString());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void when_get_contact_return_failed(){
        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.get(UUID.randomUUID().toString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Assertions.assertEquals(ApiPaths.NOT_FOUND_MSG, response.getBody().getMessage());


    }

    @Test
    void find_contact_by_id_with_invalid_id_return_IllegalArgumentException() throws Exception {

        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.of(mock(Contact.class)));


        Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                 mockContactService.get("1234");

            }
        });


    }



    @Test
    void when_update_contact_return_failed(){
        Contact contact = mock(Contact.class);
        Mockito.when(contactRepository.save(any())).thenReturn(contact);
        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.empty());

        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName("Sandra")
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.update( request,UUID.randomUUID().toString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void when_update_contact_return_success(){
        Contact contact = mock(Contact.class);
        Mockito.when(contactRepository.save(any())).thenReturn(contact);
        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.of(contact));

        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName("Sandra")
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.update( request,UUID.randomUUID().toString());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void when_edit_contact_return_failed(){
        Contact contact = mock(Contact.class);
        Mockito.when(contactRepository.save(any())).thenReturn(contact);
        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.empty());


        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.edit( UUID.randomUUID().toString(),UUID.randomUUID().toString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void when_edit_contact_return_success(){
        Contact contact = mock(Contact.class);
        Mockito.when(contactRepository.save(any())).thenReturn(contact);
        Mockito.when(contactRepository.findById(any())).thenReturn(Optional.of(contact));


        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = mockContactService.edit( UUID.randomUUID().toString(),UUID.randomUUID().toString());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
