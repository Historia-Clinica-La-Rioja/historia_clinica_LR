package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
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

    public List<ToothDrawingsBo> run(Integer patientId, List<ConsultationDentalActionBo> actions) {
        LOG.debug("Input parameters -> patientId {}, actions {}", patientId, actions);
        // TODO: load previous drawings
        List<ToothDrawingsBo> teethDrawings = computeDrawings(actions);
        // TODO: save drawings
        LOG.debug("Output size -> {}", teethDrawings.size());
        LOG.trace("Output -> {}", teethDrawings);
        return teethDrawings;
    }

    private List<ToothDrawingsBo> computeDrawings(List<ConsultationDentalActionBo> actions) {
        LOG.debug("Input parameter -> actions {}", actions);
        Map<String, ToothDrawingsBo> teethDrawings = new HashMap<>();
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

}
