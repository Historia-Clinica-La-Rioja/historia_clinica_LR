package ar.lamansys.refcounterref.infraestructure.output.repository.counterreference;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if(counterReferenceInfoBo.getFileIds() != null && !counterReferenceInfoBo.getFileIds().isEmpty()){
			referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(counterReferenceId, counterReferenceInfoBo.getFileIds());
		}
        log.debug("Output -> {}", counterReferenceId);
        return counterReferenceId;
    }

    @Override
    public Optional<CounterReferenceSummaryBo> getCounterReference(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        List<CounterReferenceSummaryBo> counterReferences = counterReferenceRepository.findByReferenceId(referenceId);
        if (!counterReferences.isEmpty()) {
			CounterReferenceSummaryBo cr = counterReferences.get(0);
			cr.setProcedures(this.getProceduresByCounterReference(cr.getId()));
			cr.setFiles(referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdAndType(cr.getId(),
                    EReferenceCounterReferenceType.CONTRARREFERENCIA.getId().intValue()));
			return Optional.of(cr);
        }
        return Optional.empty();
    }

	@Override
	public boolean existsCounterReference(Integer referenceId){
		log.debug("Input parameter -> referenceId {}", referenceId);
		return counterReferenceRepository.existsByReferenceId(referenceId);
	}

    @Override
    public List<CounterReferenceProcedureBo> getProceduresByCounterReference(Integer counterReferenceId) {
        log.debug("Input parameter -> counterReferenceId {}", counterReferenceId);
        return counterReferenceRepository.getProcedures(counterReferenceId);
    }


}