package net.pladema.internation.service.documents.epicrisis;

import net.pladema.internation.service.documents.epicrisis.domain.EpicrisisBo;

public interface UpdateEpicrisisService {

    EpicrisisBo updateDocument(Integer internmentEpisodeId, Integer patientId, EpicrisisBo epicrisisBo);
}
