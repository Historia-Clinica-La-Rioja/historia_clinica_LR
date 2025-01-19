package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import net.pladema.person.controller.dto.BasicPersonalDataDto;

@Builder
@ToString
@AllArgsConstructor
@Getter
@Setter
public class StudyOrderBasicPatientDto {

	private Integer id;

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private String identificationNumber;

	private Short identificationTypeId;

	private GenderDto gender;

	private DateDto birthDate;

	private Short patientTypeId;

}
