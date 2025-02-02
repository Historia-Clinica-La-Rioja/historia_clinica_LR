package ar.lamansys.refcounterref.application.getcounterreference;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCounterReference {

    private final CounterReferenceStorage counterReferenceStorage;

    public Optional<CounterReferenceSummaryBo> run(Integer referenceId) {
        log.debug("Input parameters -> referenceId {}", referenceId);
        return counterReferenceStorage.getCounterReference(referenceId);
    }

}
