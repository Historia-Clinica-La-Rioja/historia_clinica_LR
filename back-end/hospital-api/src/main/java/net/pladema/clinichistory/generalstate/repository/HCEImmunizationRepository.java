package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEImmunizationVo;

import java.util.List;

public interface HCEImmunizationRepository {

    List<HCEImmunizationVo> getImmunization(Integer patientId);
}
