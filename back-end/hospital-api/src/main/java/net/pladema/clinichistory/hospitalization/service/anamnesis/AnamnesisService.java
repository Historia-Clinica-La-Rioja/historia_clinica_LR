package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

public interface AnamnesisService {

    AnamnesisBo getDocument(Long documentId);
}
