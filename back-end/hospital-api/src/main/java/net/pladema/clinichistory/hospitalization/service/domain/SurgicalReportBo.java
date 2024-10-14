package net.pladema.clinichistory.hospitalization.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.validation.Valid;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.EProfessionType;
import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SurgicalReportBo extends SelfValidating<SurgicalReportBo> implements IDocumentBo {

	@Nullable
	private Long id;

	private Integer patientId;

	private PatientInfoBo patientInfo;

	private Integer encounterId;

	private Integer institutionId;

	private LocalDate patientInternmentAge;

	@Nullable
	private DiagnosisBo mainDiagnosis;

	@Nullable
	private @Valid List<DiagnosisBo> preoperativeDiagnosis = new ArrayList<>();

	@Nullable
	private @Valid List<DiagnosisBo> postoperativeDiagnosis = new ArrayList<>();

	@Nullable
	private @Valid List<ProcedureBo> surgeryProcedures = new ArrayList<>();

	@Nullable
	private LocalDateTime startDateTime;

	@Nullable
	private LocalDateTime endDateTime;

	@Nullable
	private @Valid List<ProcedureBo> procedures;

	@Nullable
	private @Valid List<ProcedureBo> anesthesia;

	@Nullable
	private @Valid List<ProcedureBo> cultures;

	@Nullable
	private @Valid List<ProcedureBo> frozenSectionBiopsies;

	@Nullable
	private @Valid List<ProcedureBo> drainages;

	@Nullable
	private @Valid ProcedureBo culture;

	@Nullable
	private @Valid ProcedureBo frozenSectionBiopsy;

	@Nullable
	private @Valid ProcedureBo drainage;

	@Nullable
	private String description;

	private LocalDateTime performedDate;

	@Nullable
	private ProsthesisInfoBo prosthesisInfo;

	@Nullable
	private Long initialDocumentId;

	@Nullable
	private String modificationReason;

	private Map<String, Object> contextMap;

	@Nullable
	private @Valid List<DocumentHealthcareProfessionalBo> surgicalTeam;

	@Nullable
	private @Valid DocumentHealthcareProfessionalBo pathologist;

	@Nullable
	private @Valid DocumentHealthcareProfessionalBo transfusionist;

	@Override
	public Integer getPatientId() {
		if (patientInfo != null) return patientInfo.getId();
		return patientId;
	}

	@Override
	public short getDocumentType() {
		return DocumentType.SURGICAL_HOSPITALIZATION_REPORT;
	}

	@Override
	public Short getDocumentSource() {
		return SourceType.HOSPITALIZATION;
	}

	@Override
	public String getProsthesisDescription() {
		return this.prosthesisInfo == null ? null : prosthesisInfo.getDescription();
	}

	public List<DocumentHealthcareProfessionalBo> getHealthcareProfessionals() {
		return Stream.of(
						surgicalTeam != null ? surgicalTeam.stream() : Stream.<DocumentHealthcareProfessionalBo>empty(),
						pathologist != null ? Stream.of(pathologist) : Stream.<DocumentHealthcareProfessionalBo>empty(),
						transfusionist != null ? Stream.of(transfusionist) : Stream.<DocumentHealthcareProfessionalBo>empty()
				)
				.flatMap(stream -> stream)
				.collect(Collectors.toList());
	}
	public void setHealthcareProfessionals(List<DocumentHealthcareProfessionalBo> healthcareProfessionals) {
		if (healthcareProfessionals != null) {
			this.surgicalTeam = healthcareProfessionals.stream()
					.filter(professionalBo -> !(EProfessionType.PATHOLOGIST.equals(professionalBo.getProfessionType())
							|| EProfessionType.TRANSFUSIONIST.equals(professionalBo.getProfessionType())))
					.collect(Collectors.toList());
			this.pathologist = healthcareProfessionals.stream()
					.filter(professionalBo -> EProfessionType.PATHOLOGIST.equals(professionalBo.getProfessionType()))
					.findFirst()
					.orElse(null);
			this.transfusionist = healthcareProfessionals.stream()
					.filter(professionalBo -> EProfessionType.TRANSFUSIONIST.equals(professionalBo.getProfessionType()))
					.findFirst()
					.orElse(null);
		}
	}

	public Boolean hasProsthesis() {
		return this.prosthesisInfo == null ? null : prosthesisInfo.getHasProsthesis();
	}
}
