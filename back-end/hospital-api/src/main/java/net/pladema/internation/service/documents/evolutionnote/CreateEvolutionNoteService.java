package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNote;

public interface CreateEvolutionNoteService {

    EvolutionNote createDocument(Integer intermentEpisodeId, Integer patientId, EvolutionNote evolutionNote);
}
