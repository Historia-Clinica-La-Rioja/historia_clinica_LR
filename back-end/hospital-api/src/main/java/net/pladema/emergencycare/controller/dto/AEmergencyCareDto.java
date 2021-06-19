package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class AEmergencyCareDto implements Serializable {

	@Nullable
	private List<SnomedDto> reasons;

	@Nullable
	private Short emergencyCareTypeId;

	@Nullable
	private Short entranceTypeId;

	@Nullable
	private PoliceInterventionDetailsDto policeInterventionDetails;

	@Nullable
	private Boolean hasPoliceIntervention;

	@Nullable
	@Length(max = 15)
	private String ambulanceCompanyId;

	@Nullable
	private AEmergencyCarePatientDto patient;
}
