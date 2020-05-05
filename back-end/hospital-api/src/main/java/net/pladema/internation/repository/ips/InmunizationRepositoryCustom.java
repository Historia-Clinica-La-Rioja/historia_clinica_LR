package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.generalstate.InmunizationVo;

import java.util.List;

public interface InmunizationRepositoryCustom {

    List<InmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
