package net.pladema.staff.application.getUserIdByHealthcareProfessionalId;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.service.HealthcareProfessionalService;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetUserIdByHealthcareProfessionalId {

	private final HealthcareProfessionalService healthcareProfessionalService;

	public Integer execute(Integer healthcareProfessionalId) {
		log.debug("Input parameters -> {}", healthcareProfessionalId);
		Integer result = healthcareProfessionalService.getUserIdHealthcareProfessionalId(healthcareProfessionalId);
		log.debug("Output -> {}", result);
		return result;
	}

}
