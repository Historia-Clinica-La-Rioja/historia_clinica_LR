package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;

import java.util.List;

public interface HCEGeneralStateService {

    List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getFamilyHistories(Integer patientId);
}
