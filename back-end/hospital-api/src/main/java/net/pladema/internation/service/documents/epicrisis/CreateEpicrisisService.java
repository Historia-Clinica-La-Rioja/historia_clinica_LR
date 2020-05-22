package net.pladema.internation.service.documents.epicrisis;

import net.pladema.internation.service.documents.epicrisis.domain.EpicrisisBo;

public interface CreateEpicrisisService {

    EpicrisisBo createDocument(Integer intermentEpisodeId, Integer patientId, EpicrisisBo epicrisisBo);
}
