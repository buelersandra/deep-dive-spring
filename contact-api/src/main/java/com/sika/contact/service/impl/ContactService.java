package com.sika.contact.service.impl;

import com.sika.contact.common.ApiPaths;
import com.sika.contact.domain.request.ContactRequestDTO;
import com.sika.contact.domain.response.BaseResponseDTO;
import com.sika.contact.domain.response.ContactResponseDTO;
import com.sika.contact.entity.Contact;
import com.sika.contact.repository.ContactRepository;
import com.sika.contact.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService implements IContactService {

    @Autowired
    ContactRepository repository;
    @Override
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> create(ContactRequestDTO contactRequestDTO) {
        Contact contact = repository.save(contactRequestDTO.toEntityMapper());
        return new ResponseEntity<>(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,contact.responseDTO()),HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> list(Integer page, Integer size,Sort.Direction sort, String sortByColumn) {
        Pageable pageable = PageRequest.of(page,size, sort,sortByColumn);
        Page<Contact> list = repository.findAll(pageable);

        Page<ContactResponseDTO> nList = list.map(Contact::responseDTO);

        return ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,nList));
    }
    @Override
    public ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> search(String word, Integer page, Integer size, Sort.Direction sort, String sortByColumn) {


        Pageable pageable = PageRequest.of(page,size, Sort.by(sort,sortByColumn));

        Page<Contact> list = repository.findAll(textInAllColumns(word),pageable);
        Page<ContactResponseDTO> nList = list.map(Contact::responseDTO);

        return ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,nList));
    }

    @Override
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> get(String id) {
        Optional<Contact> contact = repository.findById(UUID.fromString(id));
        return contact.map(value -> ResponseEntity.ok(new BaseResponseDTO<ContactResponseDTO>(ApiPaths.SUCCESS_MSG,value.responseDTO()))).orElseGet(() ->
                new ResponseEntity<>( new BaseResponseDTO<>(ApiPaths.NOT_FOUND_MSG,null),
                        HttpStatus.NOT_FOUND));
    }



    @Override
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> edit(String nationalId, String id) {
        Optional<Contact> contact = repository.findById(UUID.fromString(id));
        if(contact.isPresent()){
            Contact mContact = contact.get();
            mContact.setNationalId(nationalId);

            mContact = repository.save(mContact);
            return ResponseEntity.ok(new BaseResponseDTO<ContactResponseDTO>(ApiPaths.SUCCESS_MSG,mContact.responseDTO()));
        }

        return new ResponseEntity<>(new BaseResponseDTO<ContactResponseDTO>(ApiPaths.NOT_FOUND_MSG, null),
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> update(ContactRequestDTO contactRequestDTO, String id) {
        Optional<Contact> contact = repository.findById(UUID.fromString(id));
        if(contact.isPresent()){
            Contact mContact = contact.get();
            mContact.setLastName(contactRequestDTO.getLastName());
            mContact.setFirstName(contactRequestDTO.getFirstName());
            mContact.setGender(contactRequestDTO.getGender());
            mContact.setCountry(contactRequestDTO.getCountry());
            mContact.setNationalId(contactRequestDTO.getNationalId());
            mContact.setNationality(contactRequestDTO.getNationality());
            mContact.setDateOfBirth(contactRequestDTO.getDateOfBirth());

            mContact = repository.save(mContact);
            return ResponseEntity.ok(new BaseResponseDTO<ContactResponseDTO>(ApiPaths.SUCCESS_MSG,mContact.responseDTO()));
        }

        return new ResponseEntity<>(new BaseResponseDTO<ContactResponseDTO>(ApiPaths.NOT_FOUND_MSG, null),
                HttpStatus.NOT_FOUND);
    }


    public static <T> Specification<T> textInAllColumns(String text) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(root.getModel().getDeclaredSingularAttributes().stream().filter(
                        a -> a.getJavaType().getSimpleName().equalsIgnoreCase("string"))
                        .map(a ->
                                criteriaBuilder.like(criteriaBuilder.lower( root.get(a.getName())), finalText.toLowerCase())
                        ).toArray(Predicate[]::new));

            }
        };


    }
}
