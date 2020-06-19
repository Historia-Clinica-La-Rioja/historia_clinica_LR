package net.pladema.clinichistory.hospitalization.service;

import net.pladema.clinichistory.hospitalization.service.domain.InternmentGeneralState;

public interface InternmentStateService {

    InternmentGeneralState getInternmentGeneralState(Integer internmentEpisodeId);
}
