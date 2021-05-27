package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.odontogram.exception.ToothNotFoundException;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetToothServiceImpl implements GetToothService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ToothStorage toothStorage;

    public GetToothServiceImpl(ToothStorage toothStorage) {
        this.toothStorage = toothStorage;
    }

    @Override
    public ToothBo run(String toothId) {
        LOG.debug("Input {}", toothId);
        Optional<ToothBo> toothBoOptional = toothStorage.get(toothId);
        ToothBo result;
        if (toothBoOptional.isPresent()) result = toothBoOptional.get();
        else throw new ToothNotFoundException("El diente con id " + toothId +" no existe");
        LOG.debug("Output {}", result);
        return result;
    }
}
