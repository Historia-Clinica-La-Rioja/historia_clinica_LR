package ar.lamansys.refcounterref.application.createreference;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateReference {

    private final ReferenceStorage referenceStorage;

    public void run(Integer encounterId, Integer sourceTypeId, List<ReferenceBo> referenceBoList) {
        log.debug("Input parameters -> encounterId {}, sourceTypeId {}, referenceBoList {}", encounterId, sourceTypeId, referenceBoList);
        referenceBoList.stream().forEach(referenceBo -> {
            referenceBo.setEncounterId(encounterId);
            referenceBo.setSourceTypeId(sourceTypeId);
        });
        referenceStorage.save(referenceBoList);
    }
}
