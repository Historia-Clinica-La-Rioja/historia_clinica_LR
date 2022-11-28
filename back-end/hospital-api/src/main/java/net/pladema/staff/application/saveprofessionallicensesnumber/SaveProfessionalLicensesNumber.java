package net.pladema.staff.application.saveprofessionallicensesnumber;

import java.util.List;
import net.pladema.staff.application.saveprofessionallicensesnumber.exceptions.SaveProfessionalLicensesNumberEnumException;
import net.pladema.staff.application.saveprofessionallicensesnumber.exceptions.SaveProfessionalLicensesNumberException;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalLicenseNumberStorage;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveProfessionalLicensesNumber {

	private final ProfessionalLicenseNumberStorage professionalLicenseNumberStorage;

	private final HealthcareProfessionalService healthcareProfessionalService;

	public void run(Integer healthcareProfessionalId, List<ProfessionalLicenseNumberBo> professionalLicenseNumberBoList) {
		log.debug("Input parameters -> healthcareProfessionalId {}, professionalLicenseNumberBoList {}",healthcareProfessionalId, professionalLicenseNumberBoList);
		healthcareProfessionalService.findActiveProfessionalById(healthcareProfessionalId);
		professionalLicenseNumberStorage.getByHealthCareProfessionalId(healthcareProfessionalId)
				.forEach(oldProfession -> {
					if(professionalLicenseNumberBoList.stream().noneMatch(l-> l.equals(oldProfession)))
						professionalLicenseNumberStorage.delete(oldProfession.getId());
				});
		assertTypeNotEquals(professionalLicenseNumberBoList);
		professionalLicenseNumberBoList.forEach(professionalLicenseNumberStorage::save);
	}

	private void assertTypeNotEquals(List<ProfessionalLicenseNumberBo> licenses){
		licenses.forEach(license-> {
			if(licenses.stream().filter(l -> l.hasSameProfessionAndType(license)).count()>1)
				throw new SaveProfessionalLicensesNumberException(SaveProfessionalLicensesNumberEnumException.LICENSE_TYPE_DUPLICATED,"Tipo de matricula duplicada");
		});
	}
}
