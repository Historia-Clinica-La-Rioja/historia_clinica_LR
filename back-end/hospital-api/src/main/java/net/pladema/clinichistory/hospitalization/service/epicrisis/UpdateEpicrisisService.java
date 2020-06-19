package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface UpdateEpicrisisService {

    EpicrisisBo updateDocument(Integer internmentEpisodeId, Integer patientId, EpicrisisBo epicrisisBo);
}
