package net.pladema.snowstorm.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.snowstorm.application.getSnomedGroup.GetInstitutionPractices;

import net.pladema.snowstorm.infrastructure.input.rest.dto.SnomedRelatedGroupDto;

import net.pladema.snowstorm.infrastructure.input.rest.mapper.SnomedRelatedGroupMapper;
import net.pladema.snowstorm.services.domain.SnomedRelatedGroupBo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/institution/{institutionId}/snomed-related-group")
@Tag(name = "Snomed Related Group", description = "Snomed Related Group")
@Slf4j
@RequiredArgsConstructor
@RestController
public class SnomedRelatedGroupController {

	private final GetInstitutionPractices getSnomedRelatedGroupPractices;
	private final SnomedRelatedGroupMapper snomedRelatedGroupMapper;

	@GetMapping(value="/practices")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<SnomedRelatedGroupDto>> getPractices(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {} ", institutionId);
		List<SnomedRelatedGroupDto> result = getSnomedRelatedGroupPractices.run(institutionId).stream().map(snomedRelatedGroupMapper::fromSnomedRelatedGroupBo).collect(Collectors.toList());
		log.debug("Get practices -> ", result);
		return ResponseEntity.ok().body(result);
	}
}
