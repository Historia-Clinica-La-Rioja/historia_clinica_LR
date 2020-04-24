package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.Anamnesis;

public interface CreateAnamnesisService {

    public Anamnesis createAnanmesisDocument(Integer intermentEpisodeId, Integer patientId, Anamnesis anamnesis);
}
