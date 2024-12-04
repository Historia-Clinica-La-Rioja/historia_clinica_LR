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

	public void run(MedicationRequestBo medicationRequest) {
		log.debug("Input parameter -> medicationRequest {}", medicationRequest);
		sendMedicationRequestValidationToDispatcherPort.sendMedicationRequestToValidate(getValidationNeededData(medicationRequest));
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
		if (medicalCoverage == null || medicalCoverage.getPatientAffiliateNumber() == null)
			throw new RuntimeException("El paciente necesita tener nro de afiliado para validar la receta");
		MedicationRequestValidationDispatcherPatientBo result = patientPort.getPatientDataNeededForMedicationRequestValidation(medicationRequest.getPatientId());
		Short medicalCoverageFunderNumber = medicationRequestValidationMedicalCoveragePort.getFunderNumberByMedicalCoverageNameCuitAndAcronym(medicalCoverage);
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
		result.setArticleCode(getExternalProviderMedicationIdFromSnomedSctid.run(commercialWithPresentationSctid));
		return result;
	}

}
