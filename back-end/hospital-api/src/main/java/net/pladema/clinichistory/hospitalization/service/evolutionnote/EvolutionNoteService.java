package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

public interface EvolutionNoteService {

    EvolutionNoteBo getDocument(Long evolutionNoteId);
}
