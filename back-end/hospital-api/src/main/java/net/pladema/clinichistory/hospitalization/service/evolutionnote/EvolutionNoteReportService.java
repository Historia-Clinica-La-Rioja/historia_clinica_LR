package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

public interface EvolutionNoteReportService {

    EvolutionNoteBo getDocument(Long evolutionNoteId);
}
