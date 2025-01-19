package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class AEmergencyCareDto implements Serializable {

	@NotNull
	private String reason;

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

	@NotNull
	private AEmergencyCarePatientDto patient;
}
