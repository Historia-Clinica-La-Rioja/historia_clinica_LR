package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.exception.OdontologyException;
import ar.lamansys.odontology.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetToothSurfacesServiceImpl implements GetToothSurfacesService {

    private final GetToothWithPositionService getToothWithPositionService;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public GetToothSurfacesServiceImpl(GetToothWithPositionService getToothWithPositionService) {
        this.getToothWithPositionService = getToothWithPositionService;
    }

    @Override
    public ToothSurfacesBo run(String toothId) {
        LOG.debug("Input -> {}", toothId);
        ToothWithPositionBo toothWithPositionBo = getToothWithPositionService.run(toothId);
        var result = toothWithPositionBo.getSurfaces();
        LOG.debug("Output -> {}", result);
        return result;
    }
}
