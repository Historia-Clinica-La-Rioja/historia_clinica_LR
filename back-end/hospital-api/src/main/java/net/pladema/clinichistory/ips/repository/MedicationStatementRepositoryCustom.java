package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.generalstate.MedicationVo;

import java.util.List;

public interface MedicationStatementRepositoryCustom {

    List<MedicationVo> findGeneralState(Integer internmentEpisodeId);
}
