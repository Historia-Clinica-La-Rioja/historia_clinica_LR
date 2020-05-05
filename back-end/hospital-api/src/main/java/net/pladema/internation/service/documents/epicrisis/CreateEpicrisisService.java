package net.pladema.internation.service.documents.epicrisis;

import net.pladema.internation.service.documents.epicrisis.domain.Epicrisis;

public interface CreateEpicrisisService {

    Epicrisis createDocument(Integer intermentEpisodeId, Integer patientId, Epicrisis epicrisis);
}
