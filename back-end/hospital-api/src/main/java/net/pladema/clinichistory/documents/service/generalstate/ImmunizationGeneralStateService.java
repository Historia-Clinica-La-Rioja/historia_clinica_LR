package net.pladema.clinichistory.documents.service.generalstate;

import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;

import java.util.List;

public interface ImmunizationGeneralStateService {

    List<ImmunizationBo> getImmunizationsGeneralState(Integer internmentEpisodeId);
}
