package net.pladema.internation.service.internment;

import net.pladema.internation.service.internment.domain.InternmentGeneralState;

public interface InternmentStateService {

    InternmentGeneralState getInternmentGeneralState(Integer internmentEpisodeId);
}
