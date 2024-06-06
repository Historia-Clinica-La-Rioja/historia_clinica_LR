package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.saveinstitutionresponsibilityarea;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.port.output.SaveInstitutionResponsibilityAreaPort;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.domain.SaveInstitutionResponsibilityAreaBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveInstitutionResponsibilityArea {

	private final SaveInstitutionResponsibilityAreaPort saveInstitutionResponsibilityAreaPort;

	public Integer run(SaveInstitutionResponsibilityAreaBo saveData) {
		log.debug("Input parameters -> saveData {}", saveData);
		saveInstitutionResponsibilityAreaPort.run(saveData);
		Integer result = 1;
		log.debug("Output -> {}", result);
		return result;
	}

}
