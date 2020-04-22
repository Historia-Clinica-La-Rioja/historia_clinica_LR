package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.repository.projections.InternmentMasterDataProjection;
import net.pladema.internation.service.domain.Anamnesis;

import java.util.Collection;

public interface CreateAnamnesisService {

    public Anamnesis createAnanmesisDocument(Integer IntermentEpisodeId, Integer patientId, Anamnesis anamnesis);
}
