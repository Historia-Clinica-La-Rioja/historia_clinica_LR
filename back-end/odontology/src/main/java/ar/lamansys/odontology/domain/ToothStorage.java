package ar.lamansys.odontology.domain;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;

public interface ToothStorage {

    TeethBo getTeeth() throws ToothServiceException;

}
