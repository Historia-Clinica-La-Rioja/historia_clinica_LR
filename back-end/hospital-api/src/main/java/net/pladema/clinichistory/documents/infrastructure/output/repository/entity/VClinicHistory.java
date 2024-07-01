package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import lombok.EqualsAndHashCode;
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
import java.time.ZoneId;

@Entity
@Table(name = "v_clinic_history")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
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

	@Column(name = "internment_start_date")
	private LocalDateTime internmentStartDate;

	@Column(name = "internment_end_date")
	private LocalDateTime internmentEndDate;

	@Column(name = "emergency_care_start_date")
	private LocalDateTime emergencyCareStartDate;

	@Column(name = "emergency_care_end_date")
	private LocalDateTime emergencyCareEndDate;

	@Column(name = "service_request_end_date")
	private LocalDateTime serviceRequestEndDate;

	@Column(name = "medication_end_date")
	private LocalDateTime medicationEndDate;

	@Column(name = "patient_age_period")
	private String patientAgePeriod;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "problems", column = @Column(name = "problems")),
			@AttributeOverride( name = "familyRecord", column = @Column(name = "family_record")),
			@AttributeOverride( name = "personalRecord", column = @Column(name = "personal_record")),
			@AttributeOverride( name = "procedures", column = @Column(name = "procedures")),
			@AttributeOverride( name = "medicines", column = @Column(name = "medicines")),
			@AttributeOverride( name = "allergies", column = @Column(name = "allergies")),
			@AttributeOverride( name = "vaccines", column = @Column(name = "vaccines")),
			@AttributeOverride( name = "bloodType", column = @Column(name = "blood_type")),
			@AttributeOverride( name = "anthropometricData", column = @Column(name = "anthropometric_data")),
			@AttributeOverride( name = "epicrisisOtherCircumstances", column = @Column(name = "epicrisis_other_circumstances")),
			@AttributeOverride( name = "epicrisisExternalCause", column = @Column(name = "epicrisis_external_cause")),
			@AttributeOverride( name = "epicrisisObstetricEvent", column = @Column(name = "epicrisis_obstetric_event")),
			@AttributeOverride( name = "riskFactors", column = @Column(name = "risk_factors")),
			@AttributeOverride( name = "pediatricRiskFactors", column = @Column(name = "pediatric_risk_factors")),
			@AttributeOverride( name = "outpatientReferences", column = @Column(name = "outpatient_references")),
			@AttributeOverride( name = "serviceRequestCategory", column = @Column(name = "service_request_category")),
			@AttributeOverride( name = "serviceRequestStudies", column = @Column(name = "service_request_studies")),
			@AttributeOverride( name = "consultationReasons", column = @Column(name = "consultation_reasons")),
			@AttributeOverride( name = "odontologyProcedure", column = @Column(name = "odontology_procedure")),
			@AttributeOverride( name = "odontologyDiagnostic", column = @Column(name = "odontology_diagnostic")),
			@AttributeOverride( name = "odontologyPieces", column = @Column(name = "odontology_pieces")),
			@AttributeOverride( name = "indication", column = @Column(name = "indication")),
			@AttributeOverride( name = "referenceCounterReference", column = @Column(name = "reference_counter_reference")),
			@AttributeOverride( name = "counterReferenceClosure", column = @Column(name = "counter_reference_closure")),
			@AttributeOverride( name = "notes", column = @Column(name = "notes")),
			@AttributeOverride( name = "surgeryProcedures", column = @Column(name = "surgery_procedures")),
			@AttributeOverride( name = "anestheticHistory", column = @Column(name = "anesthetic_history")),
			@AttributeOverride( name = "preMedications", column = @Column(name = "pre_medications")),
			@AttributeOverride( name = "histories", column = @Column(name = "histories")),
			@AttributeOverride( name = "anestheticPlans", column = @Column(name = "anesthetic_plans")),
			@AttributeOverride( name = "analgesicTechniques", column = @Column(name = "analgesic_techniques")),
			@AttributeOverride( name = "anestheticTechniques", column = @Column(name = "anesthetic_techniques")),
			@AttributeOverride( name = "fluidAdministrations", column = @Column(name = "fluid_administrations")),
			@AttributeOverride( name = "anestheticAgents", column = @Column(name = "anesthetic_agents")),
			@AttributeOverride( name = "nonAnestheticDrugs", column = @Column(name = "non_anesthetic_drugs")),
			@AttributeOverride( name = "intrasurgicalAnestheticProcedures", column = @Column(name = "intrasurgical_anesthetic_procedures")),
			@AttributeOverride( name = "antibioticProphylaxis", column = @Column(name = "antibiotic_prophylaxis")),
			@AttributeOverride( name = "vitalSignsAnesthesia", column = @Column(name = "vital_signs_anesthesia")),
			@AttributeOverride( name = "postAnesthesiaStatus", column = @Column(name = "post_anesthesia_status")),
	})
	private CHDocumentSummary healthConditionSummary;

	public LocalDateTime getStartDate(){
		if (sourceTypeId.equals(ESourceType.HOSPITALIZATION.getId()) || (sourceTypeId.equals(ESourceType.ORDER.getId()) && requestSourceTypeId.equals(ESourceType.HOSPITALIZATION.getId())))
			return internmentStartDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
		if (sourceTypeId.equals(ESourceType.EMERGENCY_CARE.getId()) || (sourceTypeId.equals(ESourceType.ORDER.getId()) && requestSourceTypeId.equals(ESourceType.EMERGENCY_CARE.getId())))
			return emergencyCareStartDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
		return createdOn.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
	}

	public LocalDateTime getEndDate(){
		if (sourceTypeId.equals(ESourceType.HOSPITALIZATION.getId()) || (sourceTypeId.equals(ESourceType.ORDER.getId()) && requestSourceTypeId.equals(ESourceType.HOSPITALIZATION.getId())))
			return internmentEndDate != null ? internmentEndDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime() : null;
		if (sourceTypeId.equals(ESourceType.EMERGENCY_CARE.getId()) || (sourceTypeId.equals(ESourceType.ORDER.getId()) && requestSourceTypeId.equals(ESourceType.EMERGENCY_CARE.getId())))
			return emergencyCareEndDate != null ? emergencyCareEndDate : null;
		if (sourceTypeId.equals(ESourceType.ORDER.getId()))
			return serviceRequestEndDate != null ? serviceRequestEndDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime() : null;
		if (sourceTypeId.equals(ESourceType.RECIPE.getId()))
			return medicationEndDate != null ? medicationEndDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime() : null;
		return createdOn.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
	}

}
