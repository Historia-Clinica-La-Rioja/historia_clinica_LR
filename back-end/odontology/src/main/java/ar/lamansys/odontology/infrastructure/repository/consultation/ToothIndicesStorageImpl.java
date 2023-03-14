package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ToothIndicesStorage;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToothIndicesStorageImpl implements ToothIndicesStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ToothIndicesStorageImpl.class);

    private final ToothIndicesRepository toothIndicesRepository;

    public ToothIndicesStorageImpl(ToothIndicesRepository toothIndicesRepository) {
        this.toothIndicesRepository = toothIndicesRepository;
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

}
