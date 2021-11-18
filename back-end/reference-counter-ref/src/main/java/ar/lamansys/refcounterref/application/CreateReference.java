package ar.lamansys.refcounterref.application;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.ReferenceBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateReference {

    private final ReferenceStorage referenceStorage;

    @Transactional
    public void run(Integer encounterId, Integer sourceTypeId, List<ReferenceBo> referenceBoList) {
        log.debug("Input parameters -> encounterId {}, sourceTypeId {}, referenceBoList {}", encounterId, sourceTypeId, referenceBoList);
        referenceBoList.stream().forEach(referenceBo -> {
            referenceBo.setEncounterId(encounterId);
            referenceBo.setSourceTypeId(sourceTypeId);
        });
        referenceStorage.save(referenceBoList);
    }
}
