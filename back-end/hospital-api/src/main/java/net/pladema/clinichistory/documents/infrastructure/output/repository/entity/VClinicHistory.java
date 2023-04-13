package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "v_clinic_history")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VClinicHistory {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "source_id")
	private Integer sourceId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "document_type_id")
	private Short documentTypeId;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "source_type_id")
	private Short sourceTypeId;

	@Column(name = "request_source_id")
	private Integer requestSourceId;

	@Column(name = "clinical_specialty")
	private String clinicalSpecialty;

	@Column(name = "institution_id")
	private Integer institutionId;

	@Column(name = "institution")
	private String institution;

	@Column(name = "request_source_type_id")
	private Short requestSourceTypeId;

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;

	@Column(name = "patient_age_period")
	private String patientAgePeriod;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "problems", column = @Column(name = "problems")),
			@AttributeOverride( name = "observations", column = @Column(name = "observations")),
			@AttributeOverride( name = "procedures", column = @Column(name = "procedures")),
			@AttributeOverride( name = "riskFactors", column = @Column(name = "risk_factors")),
			@AttributeOverride( name = "observationLab", column = @Column(name = "observation_lab")),
			@AttributeOverride( name = "diagnosticReport", column = @Column(name = "diagnostic_report")),
			@AttributeOverride( name = "outpatientConsultationReasons", column = @Column(name = "outpatient_consultation_reasons")),
			@AttributeOverride( name = "odontologyProcedure", column = @Column(name = "odontology_procedure")),
			@AttributeOverride( name = "serviceRequest", column = @Column(name = "service_request")),
			@AttributeOverride( name = "odontologyDiagnostic", column = @Column(name = "odontology_diagnostic"))
	})
	private CHDocumentHealthConditionSummary healthConditionSummary;


	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "diet", column = @Column(name = "diet")),
			@AttributeOverride( name = "otherIndication", column = @Column(name = "other_indication")),
			@AttributeOverride( name = "pharmaco", column = @Column(name = "pharmaco")),
			@AttributeOverride( name = "parenteralPlan", column = @Column(name = "parenteral_plan")),
			@AttributeOverride( name = "nursingRecords", column = @Column(name = "nursing_records"))
	})
	private CHInternmentIndicationsSummary internmentIndications;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "currentIllness", column = @Column(name = "current_illness_note")),
			@AttributeOverride( name = "physicalExam", column = @Column(name = "physical_exam_note")),
			@AttributeOverride( name = "evolution", column = @Column(name = "evolution_note")),
			@AttributeOverride( name = "clinicalImpression", column = @Column(name = "clinical_impression_note")),
			@AttributeOverride( name = "otherNote", column = @Column(name = "other_note"))
	})
	private CHDocumentNotesSummary notes;

}
