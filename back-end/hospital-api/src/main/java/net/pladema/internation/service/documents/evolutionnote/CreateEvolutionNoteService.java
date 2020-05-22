package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;

public interface CreateEvolutionNoteService {

    EvolutionNoteBo createDocument(Integer intermentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNoteBo);
}
