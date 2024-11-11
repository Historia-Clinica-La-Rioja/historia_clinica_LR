package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.application.port.HistoricReferenceAdministrativeStateStorage;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation.entity.HistoricReferenceAdministrativeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoricReferenceAdministrativeStateStorageImpl implements HistoricReferenceAdministrativeStateStorage {

	private final HistoricReferenceAdministrativeStateRepository repository;
	private final ReferenceRepository referenceRepository;

	@Override
	public Integer save(Integer referenceId, Short administrativeStateId, String reason) {
		log.debug("Input parameters -> referenceId {}, administrativeStateId {}, reason {}");
		Integer result = repository.save(new HistoricReferenceAdministrativeState(referenceId, administrativeStateId, reason)).getId();
		log.debug("OUTPUT -> result {}", result);
		return result;
	}

	@Override
	@Transactional
	public void updateReferenceAdministrativeState(Integer referenceId, Short administrativeStateId, String reason) {
		log.debug("Input parameters -> referenceId {}, administrativeStateId {}, reason {}");
		Reference reference = referenceRepository.findById(referenceId).get();
		reference.setAdministrativeStateId(administrativeStateId);
		referenceRepository.save(reference);
		repository.save(new HistoricReferenceAdministrativeState(referenceId, administrativeStateId, reason));
	}

}
