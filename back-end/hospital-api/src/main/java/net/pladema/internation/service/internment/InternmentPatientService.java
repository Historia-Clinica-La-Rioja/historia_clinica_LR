package net.pladema.internation.service.internment;

import net.pladema.internation.service.internment.domain.BasicListedPatientBo;

import java.util.List;

public interface InternmentPatientService {

    List<BasicListedPatientBo> getInternmentPatients(Integer institutionId);
}
