package net.pladema.emergencycare.application.port.output;


public interface EmergencyCareEpisodeStorage {

	Boolean episodeHasEvolutionNote(Integer episodeId);

	Boolean existsDischargeForEpisode(Integer episodeId);

	Boolean existsEpisodeInOffice(Integer doctorsOfficeId, Integer shockroomId);
}
