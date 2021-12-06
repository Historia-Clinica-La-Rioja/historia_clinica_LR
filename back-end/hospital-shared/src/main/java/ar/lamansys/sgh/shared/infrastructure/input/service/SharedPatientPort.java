package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedPatientPort {

    BasicPatientDto getBasicDataFromPatient(Integer patientId);

    List<Integer> getPatientId(Short identificationTypeId, String identificationNumber, Short genderId);

    Integer createPatient(RequiredPatientDataDto requiredPatientDataDto);

    void saveMedicalCoverages(List<ExternalPatientCoverageDto> externalPatientCoverages, Integer patientId);
}
