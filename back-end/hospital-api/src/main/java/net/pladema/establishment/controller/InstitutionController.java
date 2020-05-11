package net.pladema.establishment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.establishment.controller.dto.InstitutionDto;
import net.pladema.establishment.controller.mapper.InstitutionMapper;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;

@RestController
@Api(value = "Institution", tags = { "Institution" })
@RequestMapping("institution")
public class InstitutionController {
	private final InstitutionRepository repository;
	private final InstitutionMapper mapper;

	public InstitutionController(InstitutionRepository repository, InstitutionMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@GetMapping(params = "ids")
	public @ResponseBody
	List<InstitutionDto> getMany(@RequestParam List<Integer> ids) {
		List<Integer> idsConAcceso = ids; //TODO ver permisos

		List<Institution> institutions = repository.findAllById(idsConAcceso);

		return mapper.toListInstitutionDto(institutions);
	}

}
