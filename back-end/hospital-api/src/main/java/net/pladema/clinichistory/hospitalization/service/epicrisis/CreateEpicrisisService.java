package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface CreateEpicrisisService {

    EpicrisisBo createDocument(Integer intermentEpisodeId, Integer patientId, EpicrisisBo epicrisisBo);
}
