package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.application.port.HistoricReferenceAdministrativeStateStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceAdministrativeState;
import ar.lamansys.refcounterref.domain.reference.ReferenceAdministrativeStateBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceAdministrativeStateDto;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation.entity.HistoricReferenceAdministrativeState;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoricReferenceAdministrativeStateStorageImpl implements HistoricReferenceAdministrativeStateStorage {

	private final static Integer NONE_USER = -1;
	private final static String OUTPUT = "Output -> result {}";

	private final HistoricReferenceAdministrativeStateRepository repository;
	private final ReferenceRepository referenceRepository;
	private final SharedPersonPort sharedPersonPort;

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
		log.debug("Input parameters -> referenceId {}, administrativeStateId {}, reason {}", referenceId, administrativeStateId, reason);
		Reference reference = referenceRepository.findById(referenceId).get();
		reference.setAdministrativeStateId(administrativeStateId);
		referenceRepository.save(reference);
		repository.save(new HistoricReferenceAdministrativeState(referenceId, administrativeStateId, reason));
	}

	@Override
	public Optional<ReferenceAdministrativeStateBo> getByReferenceId(Integer referenceId) {
		log.debug("Input parameters -> referenceId {}", referenceId);
		Optional<ReferenceAdministrativeStateBo> result = repository.getByReferenceId(referenceId).stream().findFirst().map(this::mapToBo);
		log.debug(OUTPUT, result);
		return result;
	}

	private ReferenceAdministrativeStateBo mapToBo(HistoricReferenceAdministrativeState entity){
		ReferenceAdministrativeStateBo result = new ReferenceAdministrativeStateBo();
		result.setId(entity.getId());
		result.setReferenceId(entity.getReferenceId());
		result.setState(EReferenceAdministrativeState.map(entity.getStateId()));
		result.setReason(entity.getReason());
		result.setCreatedOn(entity.getCreatedOn());
		result.setProfessionalName(!Objects.equals(entity.getCreatedBy(), NONE_USER) ? sharedPersonPort.getCompletePersonNameByUserId(entity.getCreatedBy()) : null);
		return result;
	}

}
