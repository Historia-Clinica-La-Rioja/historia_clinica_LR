package net.pladema.clinichistory.hospitalization.service.evolutionnote;

public interface DeleteEvolutionNoteService {

	void execute(Integer internmentEpisodeId, Long evolutionNoteId, String reason);

}
