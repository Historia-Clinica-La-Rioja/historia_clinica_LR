package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNote;

public interface UpdateEvolutionNoteService {

    EvolutionNote updateDocument(Integer internmentEpisodeId, Integer patientId, EvolutionNote evolutionNote);
}
