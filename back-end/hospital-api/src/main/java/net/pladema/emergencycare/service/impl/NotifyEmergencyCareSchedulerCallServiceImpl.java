package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.shared.domain.EmergencyCareEpisodeNotificationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.EmergencyCareEpisodeNotificationDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.NotifyEmergencyCareSchedulerCallService;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.person.service.PersonService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class NotifyEmergencyCareSchedulerCallServiceImpl implements NotifyEmergencyCareSchedulerCallService {

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private PersonService personService;

	private HospitalApiPublisher hospitalApiPublisher;

	@Override
	public void run(Integer emergencyCareEpisodeId) {
		log.debug("Input parameters -> emergencyCareEpisodeId {}", emergencyCareEpisodeId);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		EmergencyCareEpisodeNotificationBo notificationData = emergencyCareEpisodeRepository.getSchedulerNotificationData(emergencyCareEpisodeId);
		if (notificationData.getTopic() != null) {
			notificationData.setDoctorName(personService.findByHealthcareProfessionalId(doctorId).getPersonFullName());
			hospitalApiPublisher.emergencyCareAppointmentCaller(new EmergencyCareEpisodeNotificationDto(notificationData));
		}
	}

}
