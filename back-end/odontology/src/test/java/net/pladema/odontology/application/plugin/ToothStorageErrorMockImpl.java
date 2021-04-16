package net.pladema.odontology.application.plugin;

import net.pladema.odontology.application.plugin.exception.ToothServiceException;
import net.pladema.odontology.domain.TeethBo;
import net.pladema.odontology.domain.ToothStorage;
import org.springframework.stereotype.Service;

public class ToothStorageErrorMockImpl implements ToothStorage {

    @Override
    public TeethBo getTeeth() throws ToothServiceException {
        throw new ToothServiceException("Fallo algo");
    }
}
