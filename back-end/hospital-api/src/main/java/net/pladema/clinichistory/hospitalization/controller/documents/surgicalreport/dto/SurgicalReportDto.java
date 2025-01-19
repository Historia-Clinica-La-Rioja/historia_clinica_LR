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
	private DiagnosisDto mainDiagnosis;

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
	private @Valid List<HospitalizationProcedureDto> procedures = new ArrayList<>();

	@Nullable
	private @Valid List<HospitalizationProcedureDto> anesthesia = new ArrayList<>();

	@Nullable
	private @Valid List<HospitalizationProcedureDto> cultures = new ArrayList<>();

	@Nullable
	private @Valid List<HospitalizationProcedureDto> frozenSectionBiopsies = new ArrayList<>();

	@Nullable
	private @Valid List<HospitalizationProcedureDto> drainages = new ArrayList<>();

	@Nullable
	private ProsthesisInfoDto prosthesisInfo;

	@Nullable
	private String description;

	@Nullable
	private @Valid List<DocumentHealthcareProfessionalDto> surgicalTeam = new ArrayList<>();

	@Nullable
	private @Valid DocumentHealthcareProfessionalDto pathologist;

	@Nullable
	private @Valid DocumentHealthcareProfessionalDto transfusionist;

	@Nullable
	private String modificationReason;

}
