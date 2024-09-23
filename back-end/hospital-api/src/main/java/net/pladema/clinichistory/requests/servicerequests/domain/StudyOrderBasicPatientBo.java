package net.pladema.clinichistory.requests.servicerequests.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
public class StudyOrderBasicPatientBo {

	private Integer id;

	private String firstName;

	private String lastName;

	private String identificationNumber;

	private Short identificationTypeId;

	private Short genderId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
}
