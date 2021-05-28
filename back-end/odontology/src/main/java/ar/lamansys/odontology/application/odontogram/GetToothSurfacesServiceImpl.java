package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetToothSurfacesServiceImpl implements GetToothSurfacesService {

    private final GetToothWithPositionService getToothWithPositionService;

    private final Logger logger;

    public GetToothSurfacesServiceImpl(GetToothWithPositionService getToothWithPositionService) {
        this.getToothWithPositionService = getToothWithPositionService;
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public ToothSurfacesBo run(String toothId) {
        logger.debug("Input -> {}", toothId);
        ToothWithPositionBo toothWithPositionBo = getToothWithPositionService.run(toothId);
        var result = toothWithPositionBo.getSurfaces();
        logger.debug("Output -> {}", result);
        return result;
    }
}
