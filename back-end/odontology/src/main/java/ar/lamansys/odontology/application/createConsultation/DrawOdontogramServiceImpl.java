package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DrawOdontogramServiceImpl implements DrawOdontogramService {

    private static final Logger LOG = LoggerFactory.getLogger(DrawOdontogramServiceImpl.class);

    private final OdontogramDrawingStorage odontogramDrawingStorage;

    public DrawOdontogramServiceImpl(OdontogramDrawingStorage odontogramDrawingStorage) {
        this.odontogramDrawingStorage = odontogramDrawingStorage;
    }

    public List<ToothDrawingsBo> run(Integer patientId, List<ConsultationDentalActionBo> actions) {
        LOG.debug("Input parameters -> patientId {}, actions {}", patientId, actions);
        List<ToothDrawingsBo> previousDrawings = odontogramDrawingStorage.getDrawings(patientId);
        List<ToothDrawingsBo> updatedDrawings = computeDrawings(previousDrawings, actions);
        odontogramDrawingStorage.save(patientId, updatedDrawings);
        LOG.debug("Output size -> {}", updatedDrawings.size());
        LOG.trace("Output -> {}", updatedDrawings);
        return updatedDrawings;
    }

    private List<ToothDrawingsBo> computeDrawings(List<ToothDrawingsBo> previousDrawings, List<ConsultationDentalActionBo> actions) {
        LOG.debug("Input parameters -> previousDrawings {}, actions {}", previousDrawings, actions);
        if (actions == null) return new ArrayList<>();
        Map<String, ToothDrawingsBo> teethDrawings = toToothDrawingsMap(previousDrawings);
        actions.forEach(action -> {
            String toothSctid = action.getTooth().getSctid();
            ToothDrawingsBo toothDrawings = teethDrawings.get(toothSctid);
            if (toothDrawings == null) {
                toothDrawings = new ToothDrawingsBo(toothSctid);
                teethDrawings.put(toothSctid, toothDrawings);
            }
            toothDrawings.draw(action);
        });
        List<ToothDrawingsBo> result = new ArrayList<>(teethDrawings.values());
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
