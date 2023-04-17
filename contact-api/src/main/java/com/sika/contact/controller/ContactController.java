package com.sika.contact.controller;

import com.sika.contact.common.ApiPaths;
import com.sika.contact.domain.request.ContactRequestDTO;
import com.sika.contact.domain.response.BaseResponseDTO;
import com.sika.contact.domain.response.ContactResponseDTO;
import com.sika.contact.service.IContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;



@Tag(name = "Contact Management")
@RestController
@RequestMapping(ApiPaths.VERSION)
public class ContactController {

    @Autowired
    IContactService iContactService;

    //create
    @Operation(summary = "Create a contact")
    @PostMapping(value =  "/create")
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> create(@RequestBody @Validated ContactRequestDTO contactRequestDTO) {
        return iContactService.create(contactRequestDTO);
    }

    //retrieve
    @Operation(summary = "Retrieve sorted list of contacts")
    @GetMapping(value =  "/list")
    public ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> getList(
            @RequestParam(value = "page" , required = false , defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "dateCreated") String sortColumn,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") Sort.Direction sort) {
        return iContactService.list(page,size,sort,sortColumn);
    }

    //sort
    @Operation(summary = "Retrieve sorted contacts by search word")
    @GetMapping(value =  "/search")
    public ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> search(
            @RequestParam(value = "page" , required = false , defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "searchWord") String searchWord,
            @RequestParam(value = "sortColumn", required = false, defaultValue = "dateCreated") String sortColumn,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") Sort.Direction sort) {
        return iContactService.search(searchWord,page,size,sort,sortColumn);
    }


    //find
    @Operation(summary = "Get a contact By Id")
    @GetMapping(value = "/find")
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> get(@RequestParam(value = "id") String id) {
        return iContactService.get(id);
    }


    //edit
    @Operation(summary = "Edit a contact's national id")
    @PatchMapping(value = "/edit")
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> editNationalId(@RequestParam(value = "id") String id,
                                                                              @RequestParam(value = "nationalId") String nationalId) {
        return iContactService.edit(nationalId,id);
    }


    //update
    @Operation(summary = "Update contact data By Id")
    @PutMapping(value =  "/update")
    public ResponseEntity<BaseResponseDTO<ContactResponseDTO>> update(@RequestParam(value = "id" ) String id,
                                                                            @RequestBody @Validated ContactRequestDTO contactRequestDTO) {
        return iContactService.update(contactRequestDTO,id);
    }


}
