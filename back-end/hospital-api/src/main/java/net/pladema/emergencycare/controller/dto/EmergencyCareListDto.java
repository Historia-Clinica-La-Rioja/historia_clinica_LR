package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;

import java.io.Serializable;

@Value
@Builder
@ToString
@AllArgsConstructor
public class EmergencyCareListDto implements Serializable {

	private Integer id;

	private String firstname;

	private String lastname;

	private Integer patientId;

	private Short triageCategoryId;

	private EEmergencyCareType emergencyCareType;

	private EEmergencyCareState emergencyCareState;

	private Integer doctorsOffice;

	private String doctorsOfficeDescription;
}
