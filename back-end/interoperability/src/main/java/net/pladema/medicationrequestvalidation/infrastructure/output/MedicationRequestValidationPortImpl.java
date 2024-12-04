package net.pladema.medicationrequestvalidation.infrastructure.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicationrequestvalidation.application.port.output.MedicationRequestValidationPort;

import net.pladema.medicationrequestvalidation.infrastructure.output.config.MedicationRequestValidationRestClient;
import net.pladema.medicationrequestvalidation.infrastructure.output.config.MedicationRequestValidationWSConfig;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MedicationRequestValidationPortImpl implements MedicationRequestValidationPort {

	private final MedicationRequestValidationRestClient restClient;

	private final MedicationRequestValidationWSConfig medicationRequestValidationWSConfig;

	@Override
	public String validateMedicationRequest(MedicationRequestValidationDispatcherSenderBo request) {
		Object result;
		try {
			result = restClient.exchangePost(MedicationRequestValidationWSConfig.VALIDATE_PATH, parseToMap(request), Object.class);
		}
		catch (RestClientException e) {
			log.warn("Error: {}", e.getMessage());
			throw e;
		}
		return "";
	}

	private Map<String, Object> parseToMap(MedicationRequestValidationDispatcherSenderBo request) {
		Map<String, Object> result = new HashMap<>();

		Map<String, Object> patientData = getPatientDataMap(request.getPatient());
		Map<String, Object> medicalCoverage = getMedicalCoverageMap(request.getPatient().getMedicalCoverage());
		patientData.put("cobertura", medicalCoverage);
		result.put("paciente", patientData);

		Map<String, Object> healthcareProfessionalData = getHealthcareProfessionalDataMap(request.getHealthcareProfessional());
		Map<String, Object> healthcareProfessionalLicense = getHealthcareProfessionalLicenseMap(request.getHealthcareProfessional());
		healthcareProfessionalData.put("matricula", healthcareProfessionalLicense);
		result.put("medico", healthcareProfessionalData);

		List<Map<String, Object>> medications = new ArrayList<>();
		for (MedicationRequestValidationDispatcherMedicationBo medication: request.getMedications())
			medications.add(getMedicationMap(medication));
		result.put("medicamentos", medications);

		Map<String, Object> postdatedData = new HashMap<>();
		List<String> postdatedDates = mapToPostdated(request);
		postdatedData.put("fechas", postdatedDates);
		result.put("recetasPostdatadas", postdatedData);

		result.put("clienteAppId", medicationRequestValidationWSConfig.getClientId());

		Map<String, Object> institutionData = getInstitutionMap(request.getInstitution());
		result.put("subemisor", institutionData);

		return result;
	}

	private List<String> mapToPostdated(MedicationRequestValidationDispatcherSenderBo request) {
		if (request.getPostdatedDates() == null)
			return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return request.getPostdatedDates().stream()
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
		return medicationMap;
	}

	private Map<String, Object> getHealthcareProfessionalLicenseMap(MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional) {
		Map<String, Object> healthcareProfessionalLicense = new HashMap<>();
		healthcareProfessionalLicense.put("tipo", healthcareProfessional.getLicenseType());
		healthcareProfessionalLicense.put("numero", healthcareProfessional.getLicenseNumber());
		healthcareProfessionalLicense.put("provincia", "Sin especificar");
		return healthcareProfessionalLicense;
	}

	private Map<String, Object> getHealthcareProfessionalDataMap(MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional) {
		Map<String, Object> healthcareProfessionalData = new HashMap<>();
		healthcareProfessionalData.put("apellido", healthcareProfessional.getLastName());
		healthcareProfessionalData.put("nombre", healthcareProfessional.getName());
		healthcareProfessionalData.put("tipoDoc", healthcareProfessional.getIdentificationType());
		healthcareProfessionalData.put("nroDoc", healthcareProfessional.getIdentificationNumber());
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
