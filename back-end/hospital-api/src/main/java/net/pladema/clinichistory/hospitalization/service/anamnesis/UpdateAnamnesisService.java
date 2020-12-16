package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

public interface UpdateAnamnesisService {

    AnamnesisBo updateDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, AnamnesisBo anamnesisBo);
}
