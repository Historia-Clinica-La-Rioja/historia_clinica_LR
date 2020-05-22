package net.pladema.internation.service.documents.epicrisis;

import net.pladema.internation.service.documents.epicrisis.domain.EpicrisisBo;

public interface EpicrisisReportService {

    EpicrisisBo getDocument(Long epicrisisId);
}
