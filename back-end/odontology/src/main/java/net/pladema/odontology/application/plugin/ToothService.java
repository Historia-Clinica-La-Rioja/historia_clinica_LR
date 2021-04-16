package net.pladema.odontology.application.plugin;

import net.pladema.odontology.application.plugin.exception.ToothServiceException;
import net.pladema.odontology.domain.TeethBo;

public interface ToothService {

    TeethBo getTeeth() throws ToothServiceException;
}
