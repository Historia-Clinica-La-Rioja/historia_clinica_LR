package net.pladema.patient.controller.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PatientSearchFilter {

    private String lastName;

	private String firstName;

    private String middleNames;

    private String otherLastNames;
	
	private Short genderId;

	@NotNull
    private Short identificationTypeId;

    private String identificationNumber;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

	private Boolean filterByNameSelfDetermination = false;

}
