package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;

public interface CreateAnamnesisService {

    Anamnesis createDocument(Integer intermentEpisodeId, Integer patientId, Anamnesis anamnesis);
}
