package com.sika.contact.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sika.contact.common.ApiPaths;
import com.sika.contact.domain.request.ContactRequestDTO;
import com.sika.contact.domain.response.BaseResponseDTO;
import com.sika.contact.domain.response.ContactResponseDTO;
import com.sika.contact.service.IContactService;
import com.sika.contact.util.EGender;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ContactController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ContactControllerTest {

    @MockBean
    IContactService mockContactService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_contact_return_success() throws Exception {

        BaseResponseDTO<ContactResponseDTO> b = new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,ContactResponseDTO.builder().build());
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(b);

        Mockito.when(mockContactService.create(any()))
                .thenReturn(response);


        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName("Sandra")
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    //HttpMessageNotReadableException
    @Test
    void create_contact_return_HttpMessageNotReadableException() throws Exception {

        BaseResponseDTO<ContactResponseDTO> b = new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,ContactResponseDTO.builder().build());
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(b);

        Mockito.when(mockContactService.create(any()))
                .thenReturn(response);

        String requestString = "{\"firstName\":\"Sandra\",\"lastName\":\"Konotey\",\"country\":\"Ghana\"," +
                "\"nationality\":\"GHANAIAN\",\"nationalId\":\"GH1234\",\"dateOfBirth\":\"1995-01-01\"," +
                "\"gender\":\"FEMAL\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Assertions.assertEquals(result.getResolvedException().getClass(),HttpMessageNotReadableException.class);
                })
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void create_contact_with_null_return_failure_with_MethodArgumentNotValidException() throws Exception {

        BaseResponseDTO<ContactResponseDTO> b = new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,ContactResponseDTO.builder().build());
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(b);

        Mockito.when(mockContactService.create(any()))
                .thenReturn(response);


        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName(null)
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Assertions.assertEquals(result.getResolvedException().getClass(),
                            MethodArgumentNotValidException.class);
                })
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void get_contact_list_return_success() throws Exception {
        BaseResponseDTO<Page<ContactResponseDTO>> b = new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,new PageImpl<>(List.of(ContactResponseDTO.builder().build())));

        ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> page = ResponseEntity.ok(b);
        Mockito.when(mockContactService.list(anyInt(),anyInt(),any(),anyString())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/list"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void search_contact_list_return_success() throws Exception {
        BaseResponseDTO<Page<ContactResponseDTO>> b = new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG,new PageImpl<>(List.of(ContactResponseDTO.builder().build())));

        ResponseEntity<BaseResponseDTO<Page<ContactResponseDTO>>> page = ResponseEntity.ok(b);
        Mockito.when(mockContactService.search(any(),anyInt(),anyInt(),any(),anyString())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/search")
                        .param("searchWord","test"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void find_contact_by_id_with_id_return_success() throws Exception {
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.get(any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/find")
                        .param("id",UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void find_contact_by_id_without_id_return_failure() throws Exception {
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.get(any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/find"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }



    @Test
    void edit_contact_national_return_failure() throws Exception {
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.edit(any(),any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/edit")
                        .param("id", UUID.randomUUID().toString()))

                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void edit_contact_national_return_success() throws Exception {
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.edit(any(),any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/edit")
                        .param("id", UUID.randomUUID().toString())
                        .param("nationalId","GHA123"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void edit_contact_national_return_HttpRequestMethodNotSupportedException() throws Exception {
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.edit(any(),any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/edit")
                        .param("id", UUID.randomUUID().toString())
                        .param("nationalId","GHA123"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> {
                    Assertions.assertEquals(result.getResolvedException().getClass(),
                            HttpRequestMethodNotSupportedException.class);
                })
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void update_contact_return_success() throws Exception {
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.update(any(),any()))
                .thenReturn(response);

        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName("Sandra")
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/update")
                        .param("id",UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void update_contact_return_failure() throws Exception{
        ResponseEntity<BaseResponseDTO<ContactResponseDTO>> response = ResponseEntity.ok(new BaseResponseDTO<>(ApiPaths.SUCCESS_MSG, ContactResponseDTO.builder().build()));

        Mockito.when(mockContactService.update(any(),any()))
                .thenReturn(response);

        ContactRequestDTO request = ContactRequestDTO.builder()
                .firstName("Sandra")
                .lastName("Konotey")
                .country("Ghana")
                .nationalId("GH1234")
                .dateOfBirth(LocalDate.parse("1995-01-01"))
                .gender(EGender.FEMALE)
                .nationality("GHANAIAN")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }



}
