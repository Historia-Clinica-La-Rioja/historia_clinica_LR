package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.application.odontogram.GetLastActiveHistoricOdontogramDrawing;
import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class OdontogramDrawingStorageImpl implements OdontogramDrawingStorage {

    private static final Logger LOG = LoggerFactory.getLogger(OdontogramDrawingStorageImpl.class);

    private final LastOdontogramDrawingRepository lastOdontogramDrawingRepository;

	private final GetLastActiveHistoricOdontogramDrawing getLastActiveHistoricOdontogramDrawing;

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

	@Override
	public void updateConsultationId(Integer consultationId, String toothId, Integer patientId) {
		lastOdontogramDrawingRepository.getByPatientToothId(patientId, toothId).ifPresent(lod -> {
			lod.setOdontologyConsultationId(consultationId);
			lastOdontogramDrawingRepository.save(lod);
		});
	}

	private void update(LastOdontogramDrawing lod, ToothDrawingsBo toothDrawings) {
		LastOdontogramDrawing newLod = new LastOdontogramDrawing(lod.getPatientId(), toothDrawings, lod.getOdontologyConsultationId());
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

	@Override
	public void deleteByPatientId(Integer patientId) {
		LOG.debug("Input parameter -> patient {}", patientId);
		lastOdontogramDrawingRepository.deleteByPatientId(patientId);
		LOG.debug("No output");
	}

	@Override
	public void updateOdontogramDrawingFromHistoric(Integer patientId, Integer healthConditionId) {
		log.debug("Input parameters -> patient {}, healthConditionId {}", patientId, healthConditionId);
		List<String> toothIds = lastOdontogramDrawingRepository.getToothIdByHealthConditionId(healthConditionId);
		toothIds.forEach(
				toothId -> {
					getLastActiveHistoricOdontogramDrawing.run(patientId, toothId).ifPresentOrElse(historicOdontogramDrawing -> {
						save(patientId, List.of(new ToothDrawingsBo(historicOdontogramDrawing)));
						updateConsultationId(historicOdontogramDrawing.getOdontologyConsultationId(), historicOdontogramDrawing.getToothId(), patientId);
					}, () -> {
						lastOdontogramDrawingRepository.deleteByPatientIdAndToothId(patientId, toothId);
					});
				}
		);
	}
}
