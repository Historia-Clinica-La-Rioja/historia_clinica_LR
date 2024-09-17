package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.HistoricOdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class DrawOdontogramServiceImpl implements DrawOdontogramService {

    private static final Logger LOG = LoggerFactory.getLogger(DrawOdontogramServiceImpl.class);

    private final OdontogramDrawingStorage odontogramDrawingStorage;

	private final HistoricOdontogramDrawingStorage historicOdontogramDrawingStorage;

    public List<ToothDrawingsBo> run(Integer patientId, List<ConsultationDentalActionBo> actions, Integer consultationId) {
        LOG.debug("Input parameters -> patientId {}, actions {}", patientId, actions);
        List<ToothDrawingsBo> previousDrawings = odontogramDrawingStorage.getDrawings(patientId);
        List<ToothDrawingsBo> updatedDrawings = computeDrawings(previousDrawings, actions);
        odontogramDrawingStorage.save(patientId, updatedDrawings);
		updateConsultationId(consultationId, toToothDrawingsBoList(actions, new HashMap<>()), patientId);
		saveHistoricOdontogramDrawing(actions, patientId, consultationId);
        LOG.debug("Output size -> {}", updatedDrawings.size());
        LOG.trace("Output -> {}", updatedDrawings);
        return updatedDrawings;
    }

	private void updateConsultationId(Integer consultationId, List<ToothDrawingsBo> actions, Integer patientId) {
		actions.forEach(action -> {
			odontogramDrawingStorage.updateConsultationId(consultationId, action.getToothId(), patientId);
		});
	}

	private void saveHistoricOdontogramDrawing(List<ConsultationDentalActionBo> actions, Integer patientId, Integer consultationId) {
		var result = toToothDrawingsBoList(actions, new HashMap<>());
		result.forEach(
				tdb -> historicOdontogramDrawingStorage.save(new HistoricOdontogramDrawing(patientId, tdb, consultationId))
		);
	}

	private List<ToothDrawingsBo> toToothDrawingsBoList(List<ConsultationDentalActionBo> actions, Map<String, ToothDrawingsBo> teethDrawings) {
		actions.forEach(action -> {
			String toothSctid = action.getTooth().getSctid();
			ToothDrawingsBo toothDrawings = teethDrawings.get(toothSctid);
			if (toothDrawings == null) {
				toothDrawings = new ToothDrawingsBo(toothSctid);
				teethDrawings.put(toothSctid, toothDrawings);
			}
			toothDrawings.draw(action);
		});
		return new ArrayList<>(teethDrawings.values());
	}

    private List<ToothDrawingsBo> computeDrawings(List<ToothDrawingsBo> previousDrawings, List<ConsultationDentalActionBo> actions) {
        LOG.debug("Input parameters -> previousDrawings {}, actions {}", previousDrawings, actions);
        if (actions == null) return new ArrayList<>();
        Map<String, ToothDrawingsBo> teethDrawings = toToothDrawingsMap(previousDrawings);
		List<ToothDrawingsBo> result = toToothDrawingsBoList(actions, teethDrawings);
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private Map<String, ToothDrawingsBo> toToothDrawingsMap(List<ToothDrawingsBo> previousDrawings) {
        Map<String, ToothDrawingsBo> teethDrawings = new HashMap<>();
        previousDrawings.forEach(toothDrawings -> teethDrawings.put(toothDrawings.getToothId(), toothDrawings));
        return teethDrawings;
    }

}
