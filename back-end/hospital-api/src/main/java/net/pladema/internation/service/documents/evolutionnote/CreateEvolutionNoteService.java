package net.pladema.internation.service.documents.evolutionnote;

import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.internation.service.documents.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;

public interface CreateEvolutionNoteService {

    EvolutionNoteBo createDocument(Integer intermentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNoteBo);

    Long createEvolutionDiagnosis(Integer internmentEpisodeId, Integer patientId, EvolutionDiagnosisBo evolutionNote);
}
