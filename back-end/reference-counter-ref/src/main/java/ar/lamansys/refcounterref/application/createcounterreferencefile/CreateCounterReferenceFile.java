package ar.lamansys.refcounterref.application.createcounterreferencefile;

import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateCounterReferenceFile {

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

    @Transactional
    public Integer run(Integer institutionId, Integer patientId, MultipartFile file) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        return referenceCounterReferenceFileStorage.save(institutionId, patientId, file, EReferenceCounterReferenceType.CONTRARREFERENCIA.getId().intValue());
    }

}
