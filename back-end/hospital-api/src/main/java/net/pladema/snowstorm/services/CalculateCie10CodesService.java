package net.pladema.snowstorm.services;

import net.pladema.patient.controller.dto.BasicPatientDto;

import java.util.List;

public interface CalculateCie10CodesService {

    List<String> execute(String snomedCode, BasicPatientDto patient);

}
