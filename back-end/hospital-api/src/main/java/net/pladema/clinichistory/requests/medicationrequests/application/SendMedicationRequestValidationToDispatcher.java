package net.pladema.clinichistory.requests.medicationrequests.application;

import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherMedicalCoverageBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherMedicationBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.medicationrequests.application.port.output.MedicationRequestValidationMedicalCoveragePort;
import net.pladema.clinichistory.requests.medicationrequests.application.port.output.SendMedicationRequestValidationToDispatcherPort;
import ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation.EMedicationRequestValidationException;
import ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation.MedicationRequestValidationException;
import net.pladema.establishment.application.port.InstitutionPort;
import net.pladema.patient.application.port.output.PatientMedicalCoveragePort;
import net.pladema.patient.application.port.output.PatientPort;

import net.pladema.patient.domain.GetMedicalCoverageHealthInsuranceValidationDataBo;
import net.pladema.staff.application.ports.HealthcareProfessionalPort;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMedicationRequestValidationToDispatcher implements SendMedicationRequestValidation {

	private final PatientPort patientPort;

	private final HealthcareProfessionalPort healthcareProfessionalPort;

	private final InstitutionPort institutionPort;

	private final GetSnomedSctidWithPresentationFromCommercialMedicationSctid getSnomedSctidWithPresentationFromCommercialMedicationSctid;

	private final GetSnomedSctidWithPresentationFromGenericMedicationSctid getSnomedSctidWithPresentationFromGenericMedicationSctid;

	private final GetExternalProviderMedicationIdFromSnomedSctid getExternalProviderMedicationIdFromSnomedSctid;

	private final PatientMedicalCoveragePort patientMedicalCoveragePort;

	private final MedicationRequestValidationMedicalCoveragePort medicationRequestValidationMedicalCoveragePort;

	private final SendMedicationRequestValidationToDispatcherPort sendMedicationRequestValidationToDispatcherPort;

	public List<String> run(MedicationRequestBo medicationRequest) {
		log.debug("Input parameter -> medicationRequest {}", medicationRequest);
		List<String> result = sendMedicationRequestValidationToDispatcherPort.sendMedicationRequestToValidate(getValidationNeededData(medicationRequest));
		log.debug("Output -> {}", result);
		return result;
	}

	private MedicationRequestValidationDispatcherSenderBo getValidationNeededData(MedicationRequestBo medicationRequest) {
		MedicationRequestValidationDispatcherSenderBo result = new MedicationRequestValidationDispatcherSenderBo();
		result.setPatient(getPatientData(medicationRequest));
		result.setHealthcareProfessional(healthcareProfessionalPort.getProfessionalDataNeededForMedicationRequestValidation(medicationRequest.getDoctorId()));
		result.setInstitution(institutionPort.getInstitutionDataNeededForMedicationRequestValidation(medicationRequest.getInstitutionId()));
		result.setMedications(getMedicationCodes(medicationRequest.getMedications()));
		result.setPostdatedDates(getPrescriptionPostdatedDates(medicationRequest));
		return result;
	}

	private List<LocalDate> getPrescriptionPostdatedDates(MedicationRequestBo medicationRequest) {
		if (medicationRequest.getIsPostDated() == null || !medicationRequest.getIsPostDated())
			return null;
		List<LocalDate> result = new ArrayList<>();
		result.add(medicationRequest.getRequestDate().plusDays(30));
		for (int index = 1; index < medicationRequest.getRepetitions(); index++)
			result.add(result.get(index - 1).plusDays(30));
		return result;
	}

	private MedicationRequestValidationDispatcherPatientBo getPatientData(MedicationRequestBo medicationRequest) {
		GetMedicalCoverageHealthInsuranceValidationDataBo medicalCoverage = patientMedicalCoveragePort.getMedicalCoverageHealthInsuranceValidationDataById(medicationRequest.getMedicalCoverageId());
		if (medicalCoverage.getPatientAffiliateNumber() == null)
			throw new MedicationRequestValidationException("El paciente necesita tener número de afiliado para validar la receta", EMedicationRequestValidationException.NO_COVERAGE_AFFILIATE_NUMBER);
		MedicationRequestValidationDispatcherPatientBo result = patientPort.getPatientDataNeededForMedicationRequestValidation(medicationRequest.getPatientId());
		Short medicalCoverageFunderNumber = medicationRequestValidationMedicalCoveragePort.getFunderNumberByMedicalCoverageNameCuitAndAcronym(medicalCoverage);
		if (medicalCoverageFunderNumber == null)
			throw new MedicationRequestValidationException("La cobertura médica ingresada no es soportada por el sistema de validación de receta digital", EMedicationRequestValidationException.MEDICAL_COVERAGE_NOT_SUPPORTED);
		result.setMedicalCoverage(new MedicationRequestValidationDispatcherMedicalCoverageBo(medicalCoverageFunderNumber, medicalCoverage.getPatientAffiliateNumber()));
		return result;
	}

	private List<MedicationRequestValidationDispatcherMedicationBo> getMedicationCodes(List<MedicationBo> medications) {
		return medications.stream()
				.map(this::getMedicationCode)
				.collect(Collectors.toList());
	}

	private MedicationRequestValidationDispatcherMedicationBo getMedicationCode(MedicationBo medication) {
		MedicationRequestValidationDispatcherMedicationBo result = new MedicationRequestValidationDispatcherMedicationBo(medication.getCommercialMedicationPrescription().getMedicationPackQuantity());
		String commercialWithPresentationSctid = medication.getSuggestedCommercialMedication() != null ?
				getSnomedSctidWithPresentationFromCommercialMedicationSctid.run(medication.getSuggestedCommercialMedication().getSctid(), medication.getCommercialMedicationPrescription().getPresentationUnitQuantity()) :
				getSnomedSctidWithPresentationFromGenericMedicationSctid.run(medication.getSnomedSctid(), medication.getCommercialMedicationPrescription().getPresentationUnitQuantity());
		Integer medicationExternalId = getExternalProviderMedicationIdFromSnomedSctid.run(commercialWithPresentationSctid);
		if (medicationExternalId == null)
			throw new MedicationRequestValidationException("El medicamento recetado no puede ser validado por el sistema de validación de receta digital", EMedicationRequestValidationException.COULD_NOT_VALIDATE_MEDICATION);
		result.setArticleCode(medicationExternalId);
		return result;
	}

}
