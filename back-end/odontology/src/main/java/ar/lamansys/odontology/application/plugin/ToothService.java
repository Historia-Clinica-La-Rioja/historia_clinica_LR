package ar.lamansys.odontology.application.plugin;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.TeethBo;

public interface ToothService {

    TeethBo getTeeth() throws ToothServiceException;
}
