package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

public interface CreateAnamnesisService {

    AnamnesisBo createDocument(Integer intermentEpisodeId, Integer patientId, AnamnesisBo anamnesis);
}
