package net.pladema.emergencycare.service;

public interface NotifyEmergencyCareSchedulerCallService {

	void run(Integer emergencyCareEpisodeId, Integer institutionId);

}
