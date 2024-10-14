package net.pladema.parameter.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.parameter.application.GetParameterizedFormParametersByFormId;

import net.pladema.parameter.domain.ParameterCompleteDataBo;
import net.pladema.parameter.infrastructure.input.rest.dto.ParameterCompleteDataDto;

import net.pladema.parameter.infrastructure.input.rest.mapper.ParameterMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("institutions/{institutionId}/parameters")
@RestController
public class ParameterController {

	private final GetParameterizedFormParametersByFormId getParameterizedFormParametersByFormId;
	private final ParameterMapper parameterMapper;

	@GetMapping("/by-form/{formId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
	public List<ParameterCompleteDataDto> getParametersByFormId(@PathVariable("institutionId") Integer institutionId,
																@PathVariable("formId") Integer formId)
	{
		log.debug("Input parameters -> institutionId {}, formId {}", institutionId, formId);
		List<ParameterCompleteDataBo> parameters = getParameterizedFormParametersByFormId.run(formId);
		List<ParameterCompleteDataDto> result = parameterMapper.toParameterCompleteDataDto(parameters);
		log.debug("Output -> result {}", result);
		return result;
	}

}
