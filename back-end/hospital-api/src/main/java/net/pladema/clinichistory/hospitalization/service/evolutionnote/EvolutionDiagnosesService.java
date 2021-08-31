package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;

public interface EvolutionDiagnosesService {

    Long execute(Integer internmentEpisodeId, Integer patientId, EvolutionDiagnosisBo evolutionNote);
}
