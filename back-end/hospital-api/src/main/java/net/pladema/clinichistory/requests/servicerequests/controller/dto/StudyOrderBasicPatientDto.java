package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.person.controller.dto.BasicPersonalDataDto;

@Value
@Builder
@ToString
@AllArgsConstructor
public class StudyOrderBasicPatientDto {

	private final Integer id;

	private final BasicPersonalDataDto person;

}
