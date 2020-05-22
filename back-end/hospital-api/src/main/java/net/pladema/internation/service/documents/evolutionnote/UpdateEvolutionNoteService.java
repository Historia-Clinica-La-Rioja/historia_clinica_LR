package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;

public interface UpdateEvolutionNoteService {

    EvolutionNoteBo updateDocument(Integer internmentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNoteBo);
}
