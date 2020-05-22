package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;

public interface EvolutionNoteService {

    EvolutionNoteBo getDocument(Long evolutionNoteId);
}
