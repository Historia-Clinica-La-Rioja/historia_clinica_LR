package net.pladema.parameterizedform.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.parameterizedform.application.GetActiveFormsInInstitution;
import net.pladema.parameterizedform.domain.ParameterizedFormBo;
import net.pladema.parameterizedform.domain.enums.EFormScope;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;

import net.pladema.parameterizedform.infrastructure.input.rest.mapper.ParameterizedFormMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institutions/{institutionId}/parameterized-form")
@RestController
public class ParameterizedFormController {

	private final GetActiveFormsInInstitution getActiveFormsInInstitution;
	private final ParameterizedFormMapper parameterizedFormMapper;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
	public List<ParameterizedFormDto> getList(@PathVariable("institutionId") Integer institutionId){
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<ParameterizedFormBo> forms = getActiveFormsInInstitution.run(institutionId, EFormScope.OUTPATIENT);
		List<ParameterizedFormDto> result = parameterizedFormMapper.toParameterizedFormDtoList(forms);
		log.debug("Output -> result {}", result);
		return result;
	}

}
