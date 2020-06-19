package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.generalstate.InmunizationVo;

import java.util.List;

public interface InmunizationRepositoryCustom {

    List<InmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
