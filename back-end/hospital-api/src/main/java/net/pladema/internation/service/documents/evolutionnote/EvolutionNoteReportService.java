package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;

public interface EvolutionNoteReportService {

    EvolutionNoteBo getDocument(Long evolutionNoteId);
}
