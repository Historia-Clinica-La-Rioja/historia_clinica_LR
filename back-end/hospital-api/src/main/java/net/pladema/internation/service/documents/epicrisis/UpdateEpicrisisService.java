package net.pladema.internation.service.documents.epicrisis;

import net.pladema.internation.service.documents.epicrisis.domain.Epicrisis;

public interface UpdateEpicrisisService {

    Epicrisis updateDocument(Integer internmentEpisodeId, Integer patientId, Epicrisis epicrisis);
}
