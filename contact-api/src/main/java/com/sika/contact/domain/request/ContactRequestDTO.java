package com.sika.contact.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sika.contact.entity.Contact;
import com.sika.contact.util.EGender;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequestDTO {

    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Country is required")
    @NotEmpty(message = "Country is required")
    private String country;

    @NotNull(message = "Nationality is required")
    @NotEmpty(message = "Nationality is required")
    private String nationality;

    @NotNull(message = "National ID is required")
    @NotEmpty(message = "National ID is required")
    private String nationalId;

    @NotNull(message = "Date of Birth is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private EGender gender;


    @JsonIgnore
    public Contact toEntityMapper(){
        return new Contact(
                this.firstName,
                this.lastName,
                this.country,
                this.nationality,
                this.nationalId,
                this.dateOfBirth,
                this.gender);
    }
}
