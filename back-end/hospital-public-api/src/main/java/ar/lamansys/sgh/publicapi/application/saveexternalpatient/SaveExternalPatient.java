package ar.lamansys.sgh.publicapi.application.saveexternalpatient;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientExtendedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveExternalPatient {

    private final ExternalPatientStorage externalPatientStorage;

    public Integer run(ExternalPatientExtendedBo epeBo) {
        log.debug("Input parameters -> externalPatientExtendedBo {}", epeBo);
        Integer patientId = externalPatientStorage.findByExternalId(epeBo.getExternalId())
                .map(ExternalPatientBo::getPatientId)
                .orElseGet(() -> externalPatientStorage.getPatientId(epeBo)
                                .orElseGet(() ->externalPatientStorage.createPatient(epeBo)));
        epeBo.setPatientId(patientId);
        externalPatientStorage.saveMedicalCoverages(epeBo);
        externalPatientStorage.save(epeBo);
        return patientId;
    }
}
