package net.pladema.parameter.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import net.pladema.parameter.domain.ParameterBo;
import net.pladema.parameter.infrastructure.input.rest.dto.ParameterDto;
import net.pladema.parameter.infrastructure.output.BackofficeParameterStore;
import net.pladema.parameter.infrastructure.output.repository.entity.Parameter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("backoffice/parameters-autocomplete")
@RequiredArgsConstructor
@RestController
public class BackofficeParameterAutocompleteController {

	private final BackofficeParameterStore backofficeParameterStore;

	@GetMapping
	public @ResponseBody Page<ParameterBo> getList(Pageable pageable, Parameter entity) {
		var params = backofficeParameterStore.findForAutocomplete(entity.getDescription());
		return new PageImpl<>(params, pageable, params.size());
	}

	@GetMapping(params = "ids")
	public @ResponseBody List<ParameterDto> getMany(@RequestParam List<Integer> ids) {
		return backofficeParameterStore.findAllById(ids);
	}

}
