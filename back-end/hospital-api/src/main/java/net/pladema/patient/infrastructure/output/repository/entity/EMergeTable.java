package net.pladema.patient.infrastructure.output.repository.entity;

import lombok.Getter;

@Getter
public enum EMergeTable {

	DOCUMENT("document","id"),
	ADDITIONAL_DOCTOR("additional_doctor","id"),
	APPOINTMENT("appointment","id"),
	COUNTER_REFERENCE("counter_reference","id"),
	EMERGENCY_CARE_EPISODE("emergency_care_episode","id"),
	EXTERNAL_PATIENT("external_patient","external_id"),
	LAST_ODONTOGRAM_DRAWING("last_odontogram_drawing","tooth_id"),
	MEDICAL_REQUEST("medical_request","id"),
	MEDICATION_REQUEST("medication_request","id"),
	NURSING_CONSULTATION("nursing_consultation","id"),
	ODONTOLOGY_CONSULTATION("odontology_consultation","id"),
	OUTPATIENT_CONSULTATION("outpatient_consultation","id"),
	PATIENT_AUDIT("patient_audit","hospital_audit_id"),
	PATIENT_MEDICAL_COVERAGE("patient_medical_coverage","id"),
	SERVICE_REQUEST("service_request","id"),
	SNVS_REPORT("snvs_report","id"),
	TOOTH_INDICES("tooth_indices","tooth_id"),
	VACCINE_CONSULTATION("vaccine_consultation","id"),
	INTERNMENT_EPISODE("internment_episode","id"),
	HEALTH_CONDITION("health_condition","id"),
	ALLERGY_INTOLERANCE("allergy_intolerance","id"),
	INMUNIZATION("inmunization","id"),
	MEDICATION_STATEMENT("medication_statement","id"),
	PROCEDURE("procedures","id"),
	ODONTOLOGY_DIAGNOSTIC("odontology_diagnostic","id"),
	ODONTOLOGY_PROCEDURE("odontology_procedure","id"),
	OBSERVATION_VITAL_SIGN("observation_vital_sign","id"),
	OBSERVATION_LAB("observation_lab","id"),
	DIAGNOSTIC_REPORT("diagnostic_report","id"),
	INDICATION("indication","id");
	
	private String tableName;
	private String columnIdName;

	EMergeTable(String tableName, String columnIdName) {
		this.tableName = tableName;
		this.columnIdName = columnIdName;
	}
}
