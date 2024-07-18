package snomed.relations.cache.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PathVariable;

import snomed.relations.cache.application.getCommercialMedicationSnomedList.GetCommercialMedicationSnomedList;
import snomed.relations.cache.domain.GetCommercialMedicationSnomedBo;
import snomed.relations.cache.infrastructure.input.mapper.CommercialMedicationSnomedMapper;
import snomed.relations.cache.infrastructure.input.rest.dto.GetCommercialMedicationSnomedDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/commercial-medication")
@RestController
public class CommercialMedicationConceptSearchController {

	private final GetCommercialMedicationSnomedList getCommercialMedicationSnomedList;

	private final CommercialMedicationSnomedMapper commercialMedicationSnomedMapper;

	@GetMapping("/get-by-name")
	@PreAuthorize("hasPermission(#institutionId, 'PRESCRIPTOR, ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public List<GetCommercialMedicationSnomedDto> getCommercialMedicationSnomedList(@PathVariable("institutionId") Integer institutionId,
																					@RequestParam("commercialMedicationName") String commercialMedicationName) {
		log.debug("Input parameters -> institutionId {}, commercialMedicationName {}", institutionId, commercialMedicationName);
		List<GetCommercialMedicationSnomedBo> resultBo = getCommercialMedicationSnomedList.run(commercialMedicationName);
		List<GetCommercialMedicationSnomedDto> result = commercialMedicationSnomedMapper.toGetCommercialMedicationSnomedDtoList(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
