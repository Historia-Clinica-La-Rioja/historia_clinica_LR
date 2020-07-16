package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEInmunizationHistoryVo;

import java.util.List;

public interface HCEInmunizationRepository {

    List<HCEInmunizationHistoryVo> getInmunizationHistory(Integer patientId);
}
