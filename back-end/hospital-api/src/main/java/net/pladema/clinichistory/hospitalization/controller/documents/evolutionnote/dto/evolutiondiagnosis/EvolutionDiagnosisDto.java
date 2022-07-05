package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentObservationsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EvolutionDiagnosisDto implements Serializable {

	@Nullable
	private DocumentObservationsDto notes;

	@Nullable
	private List<DiagnosisDto> diagnosis = new ArrayList<>();

	@Nullable
	private HealthConditionDto mainDiagnosis;

}
