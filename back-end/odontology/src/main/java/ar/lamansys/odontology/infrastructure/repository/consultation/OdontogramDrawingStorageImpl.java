package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OdontogramDrawingStorageImpl implements OdontogramDrawingStorage {

    private static final Logger LOG = LoggerFactory.getLogger(OdontogramDrawingStorageImpl.class);

    private final LastOdontogramDrawingRepository lastOdontogramDrawingRepository;

    public OdontogramDrawingStorageImpl(LastOdontogramDrawingRepository lastOdontogramDrawingRepository) {
        this.lastOdontogramDrawingRepository = lastOdontogramDrawingRepository;
    }

    @Override
    public void save(Integer patientId, List<ToothDrawingsBo> teethDrawings) {
        LOG.debug("Input parameters -> patientId {}, teethDrawings {}", patientId, teethDrawings);
        teethDrawings.forEach(
                toothDrawings -> {
					Optional<LastOdontogramDrawing> lod = lastOdontogramDrawingRepository.getByPatientToothId(patientId, toothDrawings.getToothId());
					if (lod.isPresent())
						update(lod.get(),toothDrawings);
					else
						lastOdontogramDrawingRepository.save(new LastOdontogramDrawing(patientId, toothDrawings));
				}
        );
        LOG.debug("No output");
    }

	private void update(LastOdontogramDrawing lod, ToothDrawingsBo toothDrawings) {
		LastOdontogramDrawing newLod = new LastOdontogramDrawing(lod.getPatientId(), toothDrawings);
		newLod.setId(lod.getId());
		lastOdontogramDrawingRepository.save(newLod);
	}

    @Override
    public List<ToothDrawingsBo> getDrawings(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<ToothDrawingsBo> result = lastOdontogramDrawingRepository.getByPatientId(patientId)
                .stream()
                .map(ToothDrawingsBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
