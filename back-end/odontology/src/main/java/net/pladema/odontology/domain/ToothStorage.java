package net.pladema.odontology.domain;

import net.pladema.odontology.application.plugin.exception.ToothServiceException;

public interface ToothStorage {

    TeethBo getTeeth() throws ToothServiceException;

}
