package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface CloseEpicrisisDraftService {

	Long execute(Integer intermentEpisodeId, Long oldEpicrisisId, EpicrisisBo newEpicrisis);
	
}
