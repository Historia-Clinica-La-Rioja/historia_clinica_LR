package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.generalstate.MedicationVo;

import java.util.List;

public interface MedicationStatementRepositoryCustom {

    List<MedicationVo> findGeneralState(Integer internmentEpisodeId);
}
