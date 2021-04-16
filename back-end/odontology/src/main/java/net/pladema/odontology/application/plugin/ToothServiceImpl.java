package net.pladema.odontology.application.plugin;

import net.pladema.odontology.application.plugin.exception.ToothServiceException;
import net.pladema.odontology.domain.TeethBo;
import net.pladema.odontology.domain.ToothStorage;
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
