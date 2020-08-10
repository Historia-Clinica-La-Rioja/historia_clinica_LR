package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;

import java.util.List;

public interface ImmunizationGeneralStateService {

    List<ImmunizationBo> getImmunizationsGeneralState(Integer internmentEpisodeId);
}
