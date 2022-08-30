package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface UpdateEpicrisisService {

	Long execute(Integer intermentEpisodeId, Long oldEpicrisisId, EpicrisisBo newEpicrisis);

}
