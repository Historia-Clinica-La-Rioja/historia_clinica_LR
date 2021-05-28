package net.pladema.clinichistory.documents.service.generalstate;

import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;

import java.util.List;

public interface MedicationGeneralStateService {

    List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId);
}
