package ar.lamansys.sgh.shared.domain.medicationrequestvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequestValidationDispatcherSenderBo {

	private MedicationRequestValidationDispatcherPatientBo patient;

	private MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional;

	private MedicationRequestValidationDispatcherInstitutionBo institution;

	private List<MedicationRequestValidationDispatcherMedicationBo> medications;

	private List<LocalDate> postdatedDates;

	public Map<String, Object> parseToMap(String clientId) {
		Map<String, Object> result = new HashMap<>();

		Map<String, Object> patientData = getPatientDataMap(patient);
		Map<String, Object> medicalCoverage = getMedicalCoverageMap(patient.getMedicalCoverage());
		patientData.put("cobertura", medicalCoverage);
		result.put("paciente", patientData);

		Map<String, Object> healthcareProfessionalData = getHealthcareProfessionalDataMap(healthcareProfessional);
		Map<String, Object> healthcareProfessionalLicense = getHealthcareProfessionalLicenseMap(healthcareProfessional, institution);
		healthcareProfessionalData.put("matricula", healthcareProfessionalLicense);
		result.put("medico", healthcareProfessionalData);

		List<Map<String, Object>> medicationList = new ArrayList<>();
		for (MedicationRequestValidationDispatcherMedicationBo medication: medications)
			medicationList.add(getMedicationMap(medication));
		result.put("medicamentos", medicationList);

		if (postdatedDates != null)
			addPostdatedDatesToMap(result);

		result.put("clienteAppId", clientId);

		Map<String, Object> institutionData = getInstitutionMap(institution);
		result.put("subemisor", institutionData);

		return result;
	}

	private void addPostdatedDatesToMap(Map<String, Object> result) {
		Map<String, Object> postdatedData = new HashMap<>();
		List<String> postdatedDateList = mapToPostdated(postdatedDates);
		postdatedData.put("fechas", postdatedDateList);
		result.put("recetasPostadatas", postdatedData);
	}

	private List<String> mapToPostdated(List<LocalDate> postdatedDates) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return postdatedDates.stream()
				.map(formatter::format)
				.collect(Collectors.toList());
	}

	private Map<String, Object> getInstitutionMap(MedicationRequestValidationDispatcherInstitutionBo institution) {
		Map<String, Object> institutionData = new HashMap<>();
		institutionData.put("nombre", institution.getName());
		institutionData.put("cuit", institution.getCuit());
		institutionData.put("direccion", institution.getAddress());
		return institutionData;
	}

	private Map<String, Object> getMedicationMap(MedicationRequestValidationDispatcherMedicationBo medication) {
		Map<String, Object> medicationMap = new HashMap<>();
		medicationMap.put("regNo", medication.getArticleCode());
		medicationMap.put("cantidad", medication.getPackageQuantity());
		medicationMap.put("diagnostico", medication.getDiagnose());
		return medicationMap;
	}

	private Map<String, Object> getHealthcareProfessionalLicenseMap(MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional, MedicationRequestValidationDispatcherInstitutionBo institution) {
		Map<String, Object> healthcareProfessionalLicense = new HashMap<>();
		healthcareProfessionalLicense.put("tipo", healthcareProfessional.getLicenseType());
		healthcareProfessionalLicense.put("numero", healthcareProfessional.getLicenseNumber());
		healthcareProfessionalLicense.put("provincia", healthcareProfessional.getLicenseType().equals("MP") ? institution.getStateName() : "Sin especificar");
		return healthcareProfessionalLicense;
	}

	private Map<String, Object> getHealthcareProfessionalDataMap(MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional) {
		Map<String, Object> healthcareProfessionalData = new HashMap<>();
		healthcareProfessionalData.put("apellido", healthcareProfessional.getLastName());
		healthcareProfessionalData.put("nombre", healthcareProfessional.getName());
		healthcareProfessionalData.put("tipoDoc", healthcareProfessional.getIdentificationType());
		healthcareProfessionalData.put("nroDoc", healthcareProfessional.getIdentificationNumber());
		healthcareProfessionalData.put("email", healthcareProfessional.getEmail());
		return healthcareProfessionalData;
	}

	private Map<String, Object> getMedicalCoverageMap(MedicationRequestValidationDispatcherMedicalCoverageBo patientCoverage) {
		Map<String, Object> medicalCoverage = new HashMap<>();
		medicalCoverage.put("nroFinanciador", patientCoverage.getFunderNumber());
		medicalCoverage.put("numero", patientCoverage.getAffiliateNumber());
		return medicalCoverage;
	}

	private Map<String, Object> getPatientDataMap(MedicationRequestValidationDispatcherPatientBo patient) {
		Map<String, Object> patientData = new HashMap<>();
		patientData.put("apellido", patient.getLastName());
		patientData.put("nombre", patient.getName());
		patientData.put("tipoDoc", patient.getIdentificationType());
		patientData.put("nroDoc", patient.getIdentificationNumber());
		return patientData;
	}

}
