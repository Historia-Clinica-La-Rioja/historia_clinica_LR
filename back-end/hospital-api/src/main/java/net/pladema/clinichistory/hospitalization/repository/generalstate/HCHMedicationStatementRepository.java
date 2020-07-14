package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.MedicationVo;

import java.util.List;

public interface HCHMedicationStatementRepository {

    List<MedicationVo> findGeneralState(Integer internmentEpisodeId);
}
