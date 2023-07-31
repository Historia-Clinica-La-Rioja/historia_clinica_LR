package net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentHealthcareProfessionalDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HospitalizationProcedureDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SurgicalReportDto implements Serializable {

	@NotNull
	private boolean confirmed = false;

	@Nullable
	private @Valid List<DiagnosisDto> preoperativeDiagnosis = new ArrayList<>();

	@Nullable
	private @Valid List<DiagnosisDto> postoperativeDiagnosis = new ArrayList<>();

	@Nullable
	private @Valid List<HospitalizationProcedureDto> surgeryProcedures = new ArrayList<>();


	@Nullable
	private DateTimeDto startDateTime;

	@Nullable
	private DateTimeDto endDateTime;

	@Nullable
	private @Valid List<HospitalizationProcedureDto> procedures;

	@Nullable
	private @Valid List<HospitalizationProcedureDto> anesthesia;

	@Nullable
	private @Valid HospitalizationProcedureDto culture;

	@Nullable
	private @Valid HospitalizationProcedureDto frozenSectionBiopsy;

	@Nullable
	private @Valid HospitalizationProcedureDto drainage;

	@Nullable
	private String description;

	@Nullable
	private @Valid List<DocumentHealthcareProfessionalDto> healthcareProfessionals = new ArrayList<>();
}
