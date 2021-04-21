package ar.lamansys.odontology.application.plugin;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.TeethBo;
import ar.lamansys.odontology.domain.ToothStorage;

public class ToothStorageErrorMockImpl implements ToothStorage {

    @Override
    public TeethBo getTeeth() throws ToothServiceException {
        throw new ToothServiceException("Fallo algo");
    }
}
