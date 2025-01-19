package ar.lamansys.refcounterref.application.createreference;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateReference {

    private final ReferenceStorage referenceStorage;

    public List<Integer> run(List<CompleteReferenceBo> references) {
        log.debug("Input parameters -> references {} ", references);
        return referenceStorage.save(references);
    }
}
