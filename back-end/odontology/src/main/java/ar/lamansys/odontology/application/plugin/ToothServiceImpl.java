package ar.lamansys.odontology.application.plugin;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.TeethBo;
import ar.lamansys.odontology.domain.ToothStorage;
import org.springframework.stereotype.Service;

@Service
public class ToothServiceImpl implements ToothService {

    private final ToothStorage toothStorage;

    public ToothServiceImpl(ToothStorage toothStorage) {
        this.toothStorage = toothStorage;
    }

    @Override
    public TeethBo getTeeth() throws ToothServiceException {
        return toothStorage.getTeeth();
    }
}
