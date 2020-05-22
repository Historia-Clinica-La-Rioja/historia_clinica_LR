package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.documents.anamnesis.domain.AnamnesisBo;

public interface AnamnesisReportService {

    AnamnesisBo getDocument(Long anamnesisId);
}
