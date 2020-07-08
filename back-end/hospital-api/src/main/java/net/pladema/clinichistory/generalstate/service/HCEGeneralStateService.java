package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;

import java.util.List;

public interface HCEGeneralStateService {

    List<HCEPersonalHistoryBo> getPersonalHistory(Integer patientId);

    List<HCEPersonalHistoryBo> getFamilyHistory(Integer patientId);
}
