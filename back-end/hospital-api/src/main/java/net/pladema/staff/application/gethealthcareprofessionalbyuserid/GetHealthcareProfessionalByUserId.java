package net.pladema.staff.application.gethealthcareprofessionalbyuserid;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.service.HealthcareProfessionalService;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetHealthcareProfessionalByUserId {

	private final HealthcareProfessionalService healthcareProfessionalService;

	public Integer execute(Integer userId) {
		log.debug("Input parameters -> {}", userId);
		Integer result = healthcareProfessionalService.getProfessionalId(userId);
		log.debug("Output -> {}", result);
		return result;
	}
}
