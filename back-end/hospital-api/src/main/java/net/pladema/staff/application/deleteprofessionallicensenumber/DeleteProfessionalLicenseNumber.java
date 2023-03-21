package net.pladema.staff.application.deleteprofessionallicensenumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalLicenseNumberStorage;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteProfessionalLicenseNumber {

	private final ProfessionalLicenseNumberStorage professionalLicenseNumberStorage;

	public void run(ProfessionalLicenseNumberBo professionalLicenseNumberBoList) {
		log.debug("Input parameters -> professionalLicenseNumberBoList {}", professionalLicenseNumberBoList);
		professionalLicenseNumberStorage.delete(professionalLicenseNumberBoList.getId());
	}

}
