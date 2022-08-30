package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

public interface UpdateEvolutionNoteService {

	Long execute(Integer intermentEpisodeId, Long oldEvolutionId, EvolutionNoteBo newEvolution);

}
