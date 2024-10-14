package net.pladema.emergencycare.application.port.output;


import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import java.util.Optional;

public interface EmergencyCareEpisodeStorage {

	Boolean episodeHasEvolutionNote(Integer episodeId);

	Boolean existsDischargeForEpisode(Integer episodeId);

	Boolean existsEpisodeInOffice(Integer doctorsOfficeId, Integer shockroomId);

	Optional<EmergencyCareBo> getByBedIdInAttention(Integer bedId);

	ProfessionalPersonBo getProfessionalByEpisodeId(Integer id);
	Optional<EmergencyCareBo> getByShockroomIdInAttention(Integer shockroomId);
	Optional<EmergencyCareBo> getByDoctorsOfficeIdInAttention(Integer doctorsOfficeId);
}
