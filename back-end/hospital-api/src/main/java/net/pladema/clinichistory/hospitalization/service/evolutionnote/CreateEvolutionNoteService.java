package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;

public interface CreateEvolutionNoteService {

    EvolutionNoteBo createDocument(Integer intermentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNoteBo);

    Long createEvolutionDiagnosis(Integer internmentEpisodeId, Integer patientId, EvolutionDiagnosisBo evolutionNote);
}
