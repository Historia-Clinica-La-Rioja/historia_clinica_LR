package net.pladema.internation.service;

import net.pladema.internation.service.domain.internment.BasicListedPatientBo;

import java.util.List;

public interface InternmentPatientService {

    List<BasicListedPatientBo> getInternmentPatients(Integer institutionId);
}
