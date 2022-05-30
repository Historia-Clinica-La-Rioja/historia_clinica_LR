package net.pladema.clinichistory.hospitalization.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PatientDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String nameSelfDetermination;

	private Short identificationTypeId;

	private String identificationNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

}
