package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

public interface AnamnesisReportService {

    AnamnesisBo getDocument(Long anamnesisId);
}
