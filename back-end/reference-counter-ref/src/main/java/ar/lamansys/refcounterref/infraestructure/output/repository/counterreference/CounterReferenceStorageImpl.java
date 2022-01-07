package ar.lamansys.refcounterref.infraestructure.output.repository.counterreference;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CounterReferenceStorageImpl implements CounterReferenceStorage {

    private final CounterReferenceRepository counterReferenceRepository;
    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

    @Override
    public Integer save(CounterReferenceInfoBo counterReferenceInfoBo) {
        log.debug("Input parameters -> counterReferenceInfoBo {}", counterReferenceInfoBo);
        CounterReference counterReference = new CounterReference(counterReferenceInfoBo);
        Integer counterReferenceId = counterReferenceRepository.save(counterReference).getId();
        referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(counterReferenceId, counterReferenceInfoBo.getFileIds());
        log.debug("Output -> {}", counterReferenceId);
        return counterReferenceId;
    }

}