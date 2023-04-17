package com.sika.contact.service;

import com.sika.contact.domain.request.ContactRequestDTO;
import com.sika.contact.domain.response.BaseResponseDTO;
import com.sika.contact.domain.response.ContactResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

public interface IContactService {

    ResponseEntity<BaseResponseDTO<ContactResponseDTO>> create(ContactRequestDTO contactRequestDTO);

    ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> list(Integer page, Integer size,Sort.Direction sort, String sortByColumn);

    ResponseEntity<BaseResponseDTO<ContactResponseDTO>> get(String id);

    ResponseEntity<BaseResponseDTO<ContactResponseDTO>> edit(String nationalId, String id);

    ResponseEntity<BaseResponseDTO<ContactResponseDTO>> update(ContactRequestDTO contactRequestDTO, String id);

    ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> search(String word, Integer page, Integer size, Sort.Direction sort, String sortByColumn);
}
