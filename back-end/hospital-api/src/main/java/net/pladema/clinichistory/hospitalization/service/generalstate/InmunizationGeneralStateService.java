package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.ips.service.domain.InmunizationBo;

import java.util.List;

public interface InmunizationGeneralStateService {

    List<InmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId);
}
