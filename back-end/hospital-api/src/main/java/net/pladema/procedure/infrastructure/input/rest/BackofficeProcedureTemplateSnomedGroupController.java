package net.pladema.procedure.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;

import net.pladema.snowstorm.repository.SnomedGroupRepository;

import net.pladema.snowstorm.repository.entity.SnomedGroup;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("backoffice/proceduretemplatesnomedgroup")
public class BackofficeProcedureTemplateSnomedGroupController {

	private final SnomedGroupRepository snomedGroupRepository;

	/**
	 * The selectable snomed groups for a procedureparameter must be limited
	 * to those contained in the SnomedECL enum.
 	 * @return
	 */
	@GetMapping(params = "!ids")
	public Page<SnomedGroup> findAll(Pageable pageable, SnomedGroup entity) {
		String description = entity.getDescription();
		List<String> allowedDescriptions = Arrays.stream(SnomedECL.values()).map(x -> x.name()).collect(Collectors.toList());
		if (description == null) {
			return snomedGroupRepository.findByDescriptionIn(allowedDescriptions, pageable);
		}
		return snomedGroupRepository.findByDescriptionStartingWithAndDescriptionIn(description.toUpperCase(), allowedDescriptions, pageable);
	}

	@GetMapping(params = "ids")
	public @ResponseBody
	Iterable<SnomedGroup> getMany(@RequestParam List<Integer> ids) {
		return snomedGroupRepository.findAllById(ids);
	}
}

