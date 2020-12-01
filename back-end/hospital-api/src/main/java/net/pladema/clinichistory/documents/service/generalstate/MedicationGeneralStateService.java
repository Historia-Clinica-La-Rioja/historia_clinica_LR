package net.pladema.clinichistory.documents.service.generalstate;

import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;

import java.util.List;

public interface MedicationGeneralStateService {

    List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId);
}
