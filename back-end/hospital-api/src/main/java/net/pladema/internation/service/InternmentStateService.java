package net.pladema.internation.service;

import net.pladema.internation.service.domain.InternmentGeneralState;

public interface InternmentStateService {

    InternmentGeneralState getInternmentGeneralState(Integer internmentEpisodeId);
}
