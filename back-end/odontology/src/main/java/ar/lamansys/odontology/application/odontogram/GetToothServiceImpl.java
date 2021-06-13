package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.odontogram.exception.ToothExceptionEnum;
import ar.lamansys.odontology.application.odontogram.exception.ToothNotFoundException;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetToothServiceImpl implements GetToothService {

    private final Logger logger;

    private final ToothStorage toothStorage;

    public GetToothServiceImpl(ToothStorage toothStorage) {
        this.toothStorage = toothStorage;
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public ToothBo run(String toothId) {
        logger.debug("Input {}", toothId);
        ToothBo result = toothStorage.get(toothId)
                .orElseThrow(() -> new ToothNotFoundException(
                        ToothExceptionEnum.TOOTH_NOT_FOUND,
                        "El diente con id " + toothId +" no existe")
                );
        logger.debug("Output {}", result);
        return result;
    }
}
