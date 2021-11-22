package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchOdontogramDrawingsServiceImpl implements FetchOdontogramDrawingsService {

    private static final Logger LOG = LoggerFactory.getLogger(FetchOdontogramDrawingsServiceImpl.class);

    private final OdontogramDrawingStorage odontogramDrawingStorage;

    public FetchOdontogramDrawingsServiceImpl(OdontogramDrawingStorage odontogramDrawingStorage) {
        this.odontogramDrawingStorage = odontogramDrawingStorage;
    }

    @Override
    public List<ToothDrawingsBo> run(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        List<ToothDrawingsBo> result = odontogramDrawingStorage.getDrawings(patientId).stream()
                .filter(ToothDrawingsBo::hasAnyDrawing)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
