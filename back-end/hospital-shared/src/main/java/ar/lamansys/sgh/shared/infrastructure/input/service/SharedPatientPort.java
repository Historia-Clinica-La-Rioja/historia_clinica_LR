package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedPatientPort {

    BasicPatientDto getBasicDataFromPatient(Integer patientId);

}
