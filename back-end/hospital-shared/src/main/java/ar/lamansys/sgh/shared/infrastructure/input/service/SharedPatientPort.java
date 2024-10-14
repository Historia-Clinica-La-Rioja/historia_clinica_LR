package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.PatientGenderAgeDto;

import java.util.List;
import java.util.Optional;

public interface SharedPatientPort {

    BasicPatientDto getBasicDataFromPatient(Integer patientId);

    List<Integer> getPatientId(Short identificationTypeId, String identificationNumber, Short genderId);

    Integer createPatient(RequiredPatientDataDto requiredPatientDataDto);

    void saveMedicalCoverages(List<ExternalPatientCoverageDto> externalPatientCoverages, Integer patientId);

	boolean isValidatedOrPermanentPatient(Short patientTypeId);

	Optional<PatientGenderAgeDto> getPatientGenderAge(Integer patientId);

}
