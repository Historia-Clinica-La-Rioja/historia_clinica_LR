package net.pladema.staff.application.getprofessionsbyprofessional;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.ProfessionalProfessionsBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetProfessionsByProfessional {

	private final HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;

	public List<ProfessionalProfessionsBo> run(Integer professionalId) {
		log.debug("Input parameters -> {}", professionalId);
		List<ProfessionalProfessionsBo> result = healthcareProfessionalSpecialtyService.getProfessionsByProfessional(professionalId);
		log.debug("Output -> {}", result);
		return result;
	}
}
