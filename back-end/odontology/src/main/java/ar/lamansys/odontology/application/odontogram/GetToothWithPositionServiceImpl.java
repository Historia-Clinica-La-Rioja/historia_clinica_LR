package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.exception.OdontologyException;
import ar.lamansys.odontology.domain.OdontogramQuadrantData;
import ar.lamansys.odontology.domain.ToothWithPositionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetToothWithPositionServiceImpl implements GetToothWithPositionService {

    private final GetToothService getToothService;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public GetToothWithPositionServiceImpl(GetToothService getToothService) {
        this.getToothService = getToothService;
    }


    @Override
    public ToothWithPositionBo run(String toothId){
        LOG.debug("Input -> {}", toothId);
        var tooth = getToothService.run(toothId);
        var quadrant = OdontogramQuadrantData.getAsMap().get(tooth.getQuadrantCode());
        ToothWithPositionBo result = new ToothWithPositionBo(tooth, quadrant);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
