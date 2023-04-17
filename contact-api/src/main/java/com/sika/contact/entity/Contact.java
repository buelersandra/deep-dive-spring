package com.sika.contact.entity;

import com.sika.contact.domain.response.ContactResponseDTO;
import com.sika.contact.util.EGender;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



import java.time.LocalDate;

@Entity
@Table(name = "contact",uniqueConstraints = @UniqueConstraint(columnNames={
        "nationalId","nationality"
}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends AbstractDomain {

    @Column(nullable = false)
    @NotEmpty(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Column(nullable = false)
    @NotEmpty(message = "Country is required")
    private String country;

    @Column(nullable = false)
    @NotEmpty(message = "Nationality is required")
    private String nationality;

    @Column(nullable = false)
    @NotEmpty(message = "National ID is required")
    private String nationalId;

    @Column(nullable = false)
    @NotNull(message = "Date of Birth is required")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private EGender gender;


    public ContactResponseDTO responseDTO(){
        return new ContactResponseDTO(this.getId(),
                this.firstName,
                this.lastName,this.country,this.nationality,this.nationalId,this.dateOfBirth,this.gender);
    }


}
