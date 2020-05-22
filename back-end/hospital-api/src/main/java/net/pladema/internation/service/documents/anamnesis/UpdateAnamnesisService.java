package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.documents.anamnesis.domain.AnamnesisBo;

public interface UpdateAnamnesisService {

    AnamnesisBo updateDocument(Integer internmentEpisodeId, Integer patientId, AnamnesisBo anamnesisBo);
}
