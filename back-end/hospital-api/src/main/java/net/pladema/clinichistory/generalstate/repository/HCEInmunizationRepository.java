package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEInmunizationVo;

import java.util.List;

public interface HCEInmunizationRepository {

    List<HCEInmunizationVo> getInmunization(Integer patientId);
}
