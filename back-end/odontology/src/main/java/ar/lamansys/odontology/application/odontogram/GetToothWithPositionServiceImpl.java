package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.ToothWithPositionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetToothWithPositionServiceImpl implements GetToothWithPositionService {

    private final GetToothService getToothService;
    private final Logger logger;

    public GetToothWithPositionServiceImpl(GetToothService getToothService) {
        this.getToothService = getToothService;
        logger = LoggerFactory.getLogger(getClass());
    }


    @Override
    public ToothWithPositionBo run(String toothId){
        logger.debug("Input -> {}", toothId);
        var tooth = getToothService.run(toothId);
        var quadrant = OdontogramQuadrantBo.getQuadrant(tooth.getQuadrantCode());
        ToothWithPositionBo result = new ToothWithPositionBo(tooth, quadrant);
        logger.debug("Output -> {}", result);
        return result;
    }
}
