package com.sika.contact.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sika.contact.util.EGender;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponseDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String country;
    private String nationality;
    private String nationalId;
    private LocalDate dateOfBirth;
    private EGender gender;

}
