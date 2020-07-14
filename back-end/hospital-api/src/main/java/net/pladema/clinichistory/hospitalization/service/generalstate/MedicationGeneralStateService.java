package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.ips.service.domain.MedicationBo;

import java.util.List;

public interface MedicationGeneralStateService {

    List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId);
}
