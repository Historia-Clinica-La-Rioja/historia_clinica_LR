package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface EpicrisisReportService {

    EpicrisisBo getDocument(Long epicrisisId);
}
