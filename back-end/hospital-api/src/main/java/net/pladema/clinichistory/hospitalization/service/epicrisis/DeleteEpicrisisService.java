package net.pladema.clinichistory.hospitalization.service.epicrisis;

public interface DeleteEpicrisisService {

	void execute(Integer intermentEpisodeId, Long epicrisisId, String reason);

}
