package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

public interface UpdateEvolutionNoteService {

    EvolutionNoteBo updateDocument(Integer internmentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNoteBo);
}
