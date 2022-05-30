package net.pladema.clinichistory.hospitalization.service.anamnesis;

public interface DeleteAnamnesisService {

	void execute(Integer intermentEpisodeId, Long anamnesisId, String reason);

}
