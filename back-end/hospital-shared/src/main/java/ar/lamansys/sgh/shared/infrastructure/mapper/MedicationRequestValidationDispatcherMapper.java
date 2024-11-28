package ar.lamansys.sgh.shared.infrastructure.mapper;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherInstitutionBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherMedicalCoverageBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherMedicationBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherProfessionalBo;
import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherMedicalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherMedicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation.MedicationRequestValidationDispatcherSenderDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationRequestValidationDispatcherMapper {

	public MedicationRequestValidationDispatcherSenderBo fromMedicationRequestValidationDispatcherSenderDto(MedicationRequestValidationDispatcherSenderDto request) {
		MedicationRequestValidationDispatcherSenderBo result = new MedicationRequestValidationDispatcherSenderBo();
		result.setPatient(mapToMedicationRequestValidationDispatcherPatientBo(request.getPatient()));
		result.setHealthcareProfessional(mapToMedicationRequestValidationDispatcherProfessionalBo(request.getHealthcareProfessional()));
		result.setInstitution(mapToMedicationRequestValidationDispatcherInstitutionBo(request.getInstitution()));
		result.setMedications(mapToMedicationRequestValidationDispatcherMedicationBoList(request.getMedications()));
		result.setPostdatedDates(request.getPostdatedDates());
		return result;
	}

	private MedicationRequestValidationDispatcherInstitutionBo mapToMedicationRequestValidationDispatcherInstitutionBo(MedicationRequestValidationDispatcherInstitutionDto institution) {
		MedicationRequestValidationDispatcherInstitutionBo result = new MedicationRequestValidationDispatcherInstitutionBo();
		result.setName(institution.getName());
		result.setAddress(institution.getAddress());
		result.setCuit(institution.getCuit());
		return result;
	}

	private MedicationRequestValidationDispatcherProfessionalBo mapToMedicationRequestValidationDispatcherProfessionalBo(MedicationRequestValidationDispatcherProfessionalDto healthcareProfessional) {
		MedicationRequestValidationDispatcherProfessionalBo result = new MedicationRequestValidationDispatcherProfessionalBo();
		result.setName(healthcareProfessional.getName());
		result.setLastName(healthcareProfessional.getLastName());
		result.setIdentificationType(healthcareProfessional.getIdentificationType());
		result.setIdentificationNumber(healthcareProfessional.getIdentificationNumber());
		return result;
	}

	private MedicationRequestValidationDispatcherPatientBo mapToMedicationRequestValidationDispatcherPatientBo(MedicationRequestValidationDispatcherPatientDto patient) {
		MedicationRequestValidationDispatcherPatientBo result = new MedicationRequestValidationDispatcherPatientBo();
		result.setName(patient.getName());
		result.setLastName(patient.getLastName());
		result.setIdentificationType(patient.getIdentificationType());
		result.setIdentificationNumber(patient.getIdentificationNumber());
		result.setMedicalCoverage(new MedicationRequestValidationDispatcherMedicalCoverageBo(patient.getMedicalCoverage().getDispatcherNumber(), patient.getMedicalCoverage().getPlan()));
		return result;
	}

	private List<MedicationRequestValidationDispatcherMedicationBo> mapToMedicationRequestValidationDispatcherMedicationBoList(List<MedicationRequestValidationDispatcherMedicationDto> medications) {
		return medications.stream()
				.map(this::mapToMedicationRequestValidationDispatcherMedicationBo)
				.collect(Collectors.toList());
	}

	private MedicationRequestValidationDispatcherMedicationBo mapToMedicationRequestValidationDispatcherMedicationBo(MedicationRequestValidationDispatcherMedicationDto medication) {
		return new MedicationRequestValidationDispatcherMedicationBo(medication.getArticleCode(), medication.getQuantity());
	}

	public MedicationRequestValidationDispatcherSenderDto toMedicationRequestValidationDispatcherSenderDto(MedicationRequestValidationDispatcherSenderBo request) {
		MedicationRequestValidationDispatcherSenderDto result = new MedicationRequestValidationDispatcherSenderDto();
		result.setPatient(mapToMedicationRequestValidationDispatcherPatientDto(request.getPatient()));
		result.setHealthcareProfessional(mapToMedicationRequestValidationDispatcherProfessionalDto(request.getHealthcareProfessional()));
		result.setInstitution(mapToMedicationRequestValidationDispatcherInstitutionDto(request.getInstitution()));
		result.setMedications(mapToMedicationRequestValidationDispatcherMedicationDtoList(request.getMedications()));
		result.setPostdatedDates(request.getPostdatedDates());
		return result;
	}

	private MedicationRequestValidationDispatcherInstitutionDto mapToMedicationRequestValidationDispatcherInstitutionDto(MedicationRequestValidationDispatcherInstitutionBo institution) {
		MedicationRequestValidationDispatcherInstitutionDto result = new MedicationRequestValidationDispatcherInstitutionDto();
		result.setName(institution.getName());
		result.setAddress(institution.getAddress());
		result.setCuit(institution.getCuit());
		return result;
	}

	private MedicationRequestValidationDispatcherProfessionalDto mapToMedicationRequestValidationDispatcherProfessionalDto(MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional) {
		MedicationRequestValidationDispatcherProfessionalDto result = new MedicationRequestValidationDispatcherProfessionalDto();
		result.setName(healthcareProfessional.getName());
		result.setLastName(healthcareProfessional.getLastName());
		result.setIdentificationType(healthcareProfessional.getIdentificationType());
		result.setIdentificationNumber(healthcareProfessional.getIdentificationNumber());
		return result;
	}

	private MedicationRequestValidationDispatcherPatientDto mapToMedicationRequestValidationDispatcherPatientDto(MedicationRequestValidationDispatcherPatientBo patient) {
		MedicationRequestValidationDispatcherPatientDto result = new MedicationRequestValidationDispatcherPatientDto();
		result.setName(patient.getName());
		result.setLastName(patient.getLastName());
		result.setIdentificationType(patient.getIdentificationType());
		result.setIdentificationNumber(patient.getIdentificationNumber());
		result.setMedicalCoverage(new MedicationRequestValidationDispatcherMedicalCoverageDto(patient.getMedicalCoverage().getDispatcherNumber(), patient.getMedicalCoverage().getPlan()));
		return result;
	}

	private List<MedicationRequestValidationDispatcherMedicationDto> mapToMedicationRequestValidationDispatcherMedicationDtoList(List<MedicationRequestValidationDispatcherMedicationBo> medications) {
		return medications.stream()
				.map(this::mapToMedicationRequestValidationDispatcherMedicationDto)
				.collect(Collectors.toList());
	}

	private MedicationRequestValidationDispatcherMedicationDto mapToMedicationRequestValidationDispatcherMedicationDto(MedicationRequestValidationDispatcherMedicationBo medication) {
		return new MedicationRequestValidationDispatcherMedicationDto(medication.getArticleCode(), medication.getQuantity());
	}

}
