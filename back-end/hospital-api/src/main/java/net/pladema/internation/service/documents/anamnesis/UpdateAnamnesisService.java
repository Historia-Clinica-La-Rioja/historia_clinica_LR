package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;

public interface UpdateAnamnesisService {

    Anamnesis updateDocument(Integer internmentEpisodeId, Integer patientId, Anamnesis anamnesis);
}
