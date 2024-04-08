package net.pladema.clinichistory.requests.controller.dto;


import java.io.Serializable;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TranscribedPrescriptionDto implements Serializable {

	private SnomedDto healthCondition;

	private SnomedDto study;

	private String healthcareProfessionalName;

	@Nullable
	private String institutionName;

	@Nullable
	private String observations;
}
