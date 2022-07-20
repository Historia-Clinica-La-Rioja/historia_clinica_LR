package net.pladema.staff.application.getlicensenumberbyprofessional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalLicenseNumberStorage;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetLicenseNumberByProfessional {

	private final ProfessionalLicenseNumberStorage professionalLicenseNumberStorage;
	public List<ProfessionalLicenseNumberBo> run(Integer healthcareProfessionalId) {
		log.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		List<ProfessionalLicenseNumberBo> result = professionalLicenseNumberStorage.getByHealthCareProfessionalId(healthcareProfessionalId);
		log.debug("Output -> {}", result);
		return result;
	}
}
