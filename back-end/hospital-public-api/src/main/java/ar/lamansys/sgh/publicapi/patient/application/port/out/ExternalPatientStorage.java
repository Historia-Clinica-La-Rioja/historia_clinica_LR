package ar.lamansys.sgh.publicapi.patient.application.port.out;

import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.patient.domain.PersonBo;

import java.util.Optional;

public interface ExternalPatientStorage {

    Optional<ExternalPatientBo> findByExternalId(String externalId);

    String save(ExternalPatientBo externalPatientBo);

    Optional<Integer> getPatientId(ExternalPatientExtendedBo epeBo);

    Integer createPatient(ExternalPatientExtendedBo externalPatientExtendedBo);

    void saveMedicalCoverages(ExternalPatientExtendedBo epeBo);

    Optional<PersonBo> getPersonDataById(String patientId);
}
