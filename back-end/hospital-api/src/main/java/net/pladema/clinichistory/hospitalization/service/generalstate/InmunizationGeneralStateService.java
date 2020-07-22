package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;

import java.util.List;

public interface InmunizationGeneralStateService {

    List<ImmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId);
}
