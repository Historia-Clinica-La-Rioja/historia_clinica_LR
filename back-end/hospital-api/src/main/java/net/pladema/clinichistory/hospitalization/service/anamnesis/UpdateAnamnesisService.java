package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

public interface UpdateAnamnesisService {

    AnamnesisBo updateDocument(Integer internmentEpisodeId, Integer patientId, AnamnesisBo anamnesisBo);
}
