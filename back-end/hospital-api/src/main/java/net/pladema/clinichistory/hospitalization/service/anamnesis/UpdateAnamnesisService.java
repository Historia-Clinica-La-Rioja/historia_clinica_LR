package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

public interface UpdateAnamnesisService {

	Long execute(Integer intermentEpisodeId, Long oldAnamnesisId, AnamnesisBo newAnamnesis);

}
