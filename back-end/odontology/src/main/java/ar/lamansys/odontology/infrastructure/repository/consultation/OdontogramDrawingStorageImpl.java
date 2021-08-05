package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
                toothDrawings -> lastOdontogramDrawingRepository.save(new LastOdontogramDrawing(patientId, toothDrawings))
        );
        LOG.debug("No output");
    }
}
