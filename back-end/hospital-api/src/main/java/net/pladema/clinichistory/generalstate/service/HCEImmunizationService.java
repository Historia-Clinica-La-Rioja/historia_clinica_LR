package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEImmunizationBo;

import java.util.List;

public interface HCEImmunizationService {

    List<HCEImmunizationBo> getImmunization(Integer patientId);
}
