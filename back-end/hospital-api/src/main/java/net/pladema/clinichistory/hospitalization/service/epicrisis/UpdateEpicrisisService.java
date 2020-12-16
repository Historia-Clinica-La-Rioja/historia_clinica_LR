package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface UpdateEpicrisisService {

    EpicrisisBo updateDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, EpicrisisBo epicrisisBo);
}
