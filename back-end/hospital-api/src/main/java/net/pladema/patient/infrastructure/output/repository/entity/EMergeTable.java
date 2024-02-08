package net.pladema.patient.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EMergeTable {

	DOCUMENT("document","Document","id"),
	ADDITIONAL_DOCTOR("additional_doctor","AdditionalDoctor", "id"),
	APPOINTMENT("appointment", "Appointment", "id"),
	COUNTER_REFERENCE("counter_reference", "CounterReference", "id"),
	EMERGENCY_CARE_EPISODE("emergency_care_episode", "EmergencyCareEpisode", "id"),
	EXTERNAL_PATIENT("external_patient", "ExternalPatient", "external_id"),
	LAST_ODONTOGRAM_DRAWING("last_odontogram_drawing", "LastOdontogramDrawing", "tooth_id"),
	MEDICATION_REQUEST("medication_request", "MedicationRequest", "id"),
	NURSING_CONSULTATION("nursing_consultation", "NursingConsultation","id"),
	ODONTOLOGY_CONSULTATION("odontology_consultation", "OdontologyConsultation", "id"),
	OUTPATIENT_CONSULTATION("outpatient_consultation", "OutpatientConsultation", "id"),
	PATIENT_MEDICAL_COVERAGE("patient_medical_coverage", "PatientMedicalCoverageAssn", "id"),
	SERVICE_REQUEST("service_request","ServiceRequest", "id"),
	SNVS_REPORT("snvs_report", "SnvsReport", "id"),
	TOOTH_INDICES("tooth_indices","ToothIndices","tooth_id"),
	VACCINE_CONSULTATION("vaccine_consultation", "VaccineConsultation", "id"),
	INTERNMENT_EPISODE("internment_episode", "InternmentEpisode", "id"),
	HEALTH_CONDITION("health_condition","HealthCondition", "id"),
	ALLERGY_INTOLERANCE("allergy_intolerance","AllergyIntolerance", "id"),
	INMUNIZATION("inmunization","Inmunization", "id"),
	MEDICATION_STATEMENT("medication_statement", "MedicationStatement", "id"),
	PROCEDURE("procedures","Procedure", "id"),
	ODONTOLOGY_DIAGNOSTIC("odontology_diagnostic", "OdontologyDiagnostic", "id"),
	ODONTOLOGY_PROCEDURE("odontology_procedure","OdontologyProcedure", "id"),
	OBSERVATION_VITAL_SIGN("observation_vital_sign", "ObservationRiskFactor","id"),
	OBSERVATION_LAB("observation_lab","ObservationLab", "id"),
	DIAGNOSTIC_REPORT("diagnostic_report","DiagnosticReport", "id"),
	INDICATION("indication","Indication", "id"),
	PHARMACO("indication","Pharmaco", "id"),
	DIET("indication","Diet", "id"),
	OTHER_INDICATION("indication","OtherIndication", "id"),
	PARENTERAL_PLAN("indication","ParenteralPlan", "id"),
	VIOLENCE_REPORT("violence_report","ViolenceReport", "id"),
	SURGICAL_REPORT("surgical_report", "SurgicalReport", "id");

	private String tableName;
	private String entityName;
	private String columnIdName;

	EMergeTable(String tableName, String entityName, String columnIdName) {
		this.tableName = tableName;
		this.entityName = entityName;
		this.columnIdName = columnIdName;
	}

	public static EMergeTable map(String entityName) {
		for(EMergeTable e : values()) {
			if(e.entityName.equals(entityName)) return e;
		}
		throw new NotFoundException("merge-table-not-exists", String.format("La tabla %s no existe o no es migrable", entityName));
	}
}
