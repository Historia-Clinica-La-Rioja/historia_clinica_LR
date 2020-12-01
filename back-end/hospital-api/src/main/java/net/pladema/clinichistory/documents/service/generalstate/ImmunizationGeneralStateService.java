package net.pladema.clinichistory.documents.service.generalstate;

import net.pladema.clinichistory.documents.service.ips.domain.ImmunizationBo;

import java.util.List;

public interface ImmunizationGeneralStateService {

    List<ImmunizationBo> getImmunizationsGeneralState(Integer internmentEpisodeId);
}
