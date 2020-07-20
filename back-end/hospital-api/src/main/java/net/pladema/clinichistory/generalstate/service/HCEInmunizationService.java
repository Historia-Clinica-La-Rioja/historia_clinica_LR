package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEInmunizationBo;

import java.util.List;

public interface HCEInmunizationService {

    List<HCEInmunizationBo> getInmunization(Integer patientId);
}
