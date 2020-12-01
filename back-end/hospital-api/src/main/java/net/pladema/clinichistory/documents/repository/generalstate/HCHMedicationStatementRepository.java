package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.MedicationVo;

import java.util.List;

public interface HCHMedicationStatementRepository {

    List<MedicationVo> findGeneralState(Integer internmentEpisodeId);
}
