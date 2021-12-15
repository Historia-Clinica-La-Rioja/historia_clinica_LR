package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientExtendedBo;

import java.util.Optional;

public interface ExternalPatientStorage {

    Optional<ExternalPatientBo> findByExternalId(String externalId);

    String save(ExternalPatientBo externalPatientBo);

    Optional<Integer> getPatientId(ExternalPatientExtendedBo epeBo);

    Integer createPatient(ExternalPatientExtendedBo externalPatientExtendedBo);

    void saveMedicalCoverages(ExternalPatientExtendedBo epeBo);
}
