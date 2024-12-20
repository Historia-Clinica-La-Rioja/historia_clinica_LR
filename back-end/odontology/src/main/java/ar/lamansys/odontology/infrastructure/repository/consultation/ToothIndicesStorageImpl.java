package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.application.odontogram.ports.ToothIndicesStorage;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.EOdontologyIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToothIndicesStorageImpl implements ToothIndicesStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ToothIndicesStorageImpl.class);

    private final ToothIndicesRepository toothIndicesRepository;
	private final GetToothService getToothService;

    public ToothIndicesStorageImpl(ToothIndicesRepository toothIndicesRepository, GetToothService getToothService) {
        this.toothIndicesRepository = toothIndicesRepository;
		this.getToothService = getToothService;
	}

    @Override
    public void save(Integer patientId, List<ToothIndicesBo> teethIndices) {
        LOG.debug("Input parameters -> patientId {}, teethIndices {}", patientId, teethIndices);
		teethIndices.forEach( toothIndices -> {
			Optional<ToothIndices> ti = toothIndicesRepository.getByPatientToothId(patientId, toothIndices.getToothId());
			if (ti.isPresent())
				update(ti.get(),toothIndices);
			else
				toothIndicesRepository.save(new ToothIndices(patientId,toothIndices));
		});
    }

	private void update(ToothIndices ti, ToothIndicesBo toothIndicesBo) {
		ToothIndices newTi = new ToothIndices(ti.getPatientId(),toothIndicesBo);
		newTi.setId(ti.getId());
		toothIndicesRepository.save(newTi);
	}

    @Override
    public List<ToothIndicesBo> getTeethIndices(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        List<ToothIndicesBo> result = toothIndicesRepository.getByPatientId(patientId)
                .stream()
                .map(ToothIndices::toToothIndicesBo)
                .collect(Collectors.toList());
        LOG.debug("Output -> {}", result);
        return result;
    }


	@Override
	public void deleteByPatientId(Integer patientId) {
		LOG.debug("Input parameters -> patientId {}", patientId);
		toothIndicesRepository.deleteByPatientId(patientId);
		LOG.debug("Output -> No Output");
	}

	@Override
	public CpoCeoIndicesBo computeIndices(Integer patientId, List<ConsultationDentalActionBo> dentalActions) {
		LOG.debug("Input parameters -> patientId {}, dentalActions {}", patientId, dentalActions);
		Map<String, ToothIndicesBo> teethIndices = fetchPreviousTeethIndices(patientId);
		dentalActions.forEach(action -> applyDentalAction(teethIndices, action));

		CpoCeoIndicesBo result = new CpoCeoIndicesBo();
		teethIndices.values().forEach(toothIndices -> {
			EOdontologyIndexBo index = toothIndices.computeToothResultingIndex();
			if (toothIndices.isTemporary())
				updateTemporaryIndices(result, index);
			else
				updatePermanentIndices(result, index);
		});
		save(patientId, new ArrayList<>(teethIndices.values()));
		LOG.debug("Output -> {}", result);
		return result;
	}

	private void updatePermanentIndices(CpoCeoIndicesBo indices, EOdontologyIndexBo newIndex) {
		LOG.debug("Input parameters -> result {}, newIndex {}", indices, newIndex);
		switch (newIndex) {
			case CAVITIES:
				indices.setPermanentC(indices.getPermanentC() + 1);
				return;
			case LOST:
				indices.setPermanentP(indices.getPermanentP() + 1);
				return;
			case FIXED:
				indices.setPermanentO(indices.getPermanentO() + 1);
		}
	}

	private void updateTemporaryIndices(CpoCeoIndicesBo indices, EOdontologyIndexBo newIndex) {
		LOG.debug("Input parameters -> indices {}, newIndex {}", indices, newIndex);
		switch (newIndex) {
			case CAVITIES:
				indices.setTemporaryC(indices.getTemporaryC() + 1);
				return;
			case LOST:
				indices.setTemporaryE(indices.getTemporaryE() + 1);
				return;
			case FIXED:
				indices.setTemporaryO(indices.getTemporaryO() + 1);
		}
	}

	private void applyDentalAction(Map<String, ToothIndicesBo> teethIndices, ConsultationDentalActionBo action) {
		LOG.debug("Input parameters -> teethIndices {}, action {}", teethIndices, action);
		String toothId = action.getTooth().getSctid();
		ToothIndicesBo toothIndices = teethIndices.get(toothId);
		if (toothIndices == null) {
			ToothBo toothBo = getToothService.run(toothId);
			toothIndices = new ToothIndicesBo(toothId, toothBo.isTemporary());
		}
		toothIndices.apply(action);
		teethIndices.put(toothId, toothIndices);
	}

	private Map<String, ToothIndicesBo> fetchPreviousTeethIndices(Integer patientId) {
		LOG.debug("Input parameter -> patientId {}", patientId);
		List<ToothIndicesBo> teethIndicesList = getTeethIndices(patientId);
		return toToothIndicesMap(teethIndicesList);
	}

	private Map<String, ToothIndicesBo> toToothIndicesMap(List<ToothIndicesBo> teethIndicesList) {
		Map<String, ToothIndicesBo> teethIndicesMap = new HashMap<>();
		teethIndicesList.forEach(toothIndices -> teethIndicesMap.put(toothIndices.getToothId(), toothIndices));
		return teethIndicesMap;
	}

}
