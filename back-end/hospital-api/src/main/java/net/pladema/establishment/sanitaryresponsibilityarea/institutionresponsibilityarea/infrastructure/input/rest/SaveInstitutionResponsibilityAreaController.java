package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.saveinstitutionresponsibilityarea.SaveInstitutionResponsibilityArea;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.domain.SaveInstitutionResponsibilityAreaBo;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.mapper.SaveInstitutionResponsibilityAreaMapper;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.rest.dto.SaveInstitutionResponsibilityAreaDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area/save-institution-responsibility-area")
@RestController
public class SaveInstitutionResponsibilityAreaController {

	private final SaveInstitutionResponsibilityAreaMapper saveInstitutionResponsibilityAreaMapper;

	private final SaveInstitutionResponsibilityArea saveInstitutionResponsibilityArea;

	@PostMapping
	public Integer run(@PathVariable("institutionId") Integer institutionId, @RequestBody SaveInstitutionResponsibilityAreaDto saveInstitutionResponsibilityAreaDto) {
		log.debug("Input parameters -> institutionId {}, saveInstitutionResponsibilityAreaDto {}", institutionId, saveInstitutionResponsibilityAreaDto);
		SaveInstitutionResponsibilityAreaBo saveInstitutionResponsibilityAreaBo = saveInstitutionResponsibilityAreaMapper.toSaveInstitutionResponsibilityAreaBo(saveInstitutionResponsibilityAreaDto, institutionId);
		Integer result = saveInstitutionResponsibilityArea.run(saveInstitutionResponsibilityAreaBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
