package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;

public interface AnamnesisReportService {

    Anamnesis getDocument(Long anamnesisId);
}
