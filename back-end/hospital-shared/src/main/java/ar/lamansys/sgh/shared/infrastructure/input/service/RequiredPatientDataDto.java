package ar.lamansys.sgh.shared.infrastructure.input.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
public class RequiredPatientDataDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime birthDate;

    private String firstName;

    private Short genderId;

    private String identificationNumber;

    private Short identificationTypeId;

    private String lastName;

    private String phoneNumber;

    private String email;

    private Integer institutionId;
}
