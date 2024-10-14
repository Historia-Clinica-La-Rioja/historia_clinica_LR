package net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.sanitaryresponsibilityarea.application.GetPatientCoordinatesByAddedInstitution;

import net.pladema.sanitaryresponsibilityarea.application.GetPatientCoordinatesByOutpatientConsultation;
import net.pladema.sanitaryresponsibilityarea.domain.GetPatientCoordinatesByOutpatientConsultationFilterBo;
import net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo;
import net.pladema.sanitaryresponsibilityarea.domain.GetPatientCoordinatesByAddedInstitutionFilterBo;
import net.pladema.sanitaryresponsibilityarea.infrastructure.input.mapper.SanitaryResponsibilityAreaMapper;
import net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto.GetPatientCoordinatesByOutpatientConsultationFilterDto;
import net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto.SanitaryRegionPatientMapCoordinatesDto;

import net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto.GetPatientCoordinatesByAddedInstitutionFilterDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/sanitary-responsibility-area")
@RestController
public class SanitaryResponsibilityAreaController {

	private final ObjectMapper objectMapper;

	private final SanitaryResponsibilityAreaMapper sanitaryResponsibilityAreaMapper;

	private final GetPatientCoordinatesByAddedInstitution getPatientCoordinatesByAddedInstitution;

	private final GetPatientCoordinatesByOutpatientConsultation getPatientCoordinatesByOutpatientConsultation;

	@GetMapping("/get-patient-coordinated-by-added-institution")
	public List<SanitaryRegionPatientMapCoordinatesDto> getPatientCoordinatesByAddedInstitution(@PathVariable("institutionId") Integer institutionId,
																							   @RequestParam("filter") String filterString) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, filter {}", institutionId, filterString);
		GetPatientCoordinatesByAddedInstitutionFilterDto filterDto = objectMapper.readValue(filterString, GetPatientCoordinatesByAddedInstitutionFilterDto.class);
		GetPatientCoordinatesByAddedInstitutionFilterBo filter = sanitaryResponsibilityAreaMapper.fromGetPatientCoordinatesByAddedInstitutionFilterDto(institutionId, filterDto);
		List<SanitaryRegionPatientMapCoordinatesBo> resultBo = getPatientCoordinatesByAddedInstitution.run(filter);
		List<SanitaryRegionPatientMapCoordinatesDto> result = sanitaryResponsibilityAreaMapper.toGetPatientCoordinatesByAddedInstitutionDtoList(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("/get-patient-coordinated-by-outpatient-consultation")
	public List<SanitaryRegionPatientMapCoordinatesDto> getPatientCoordinatesByOutpatientConsultation(@PathVariable("institutionId") Integer institutionId,
																									  @RequestParam("filter") String filterString) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, filter {}", institutionId, filterString);
		GetPatientCoordinatesByOutpatientConsultationFilterDto filterDto = objectMapper.readValue(filterString, GetPatientCoordinatesByOutpatientConsultationFilterDto.class);
		GetPatientCoordinatesByOutpatientConsultationFilterBo filter = sanitaryResponsibilityAreaMapper.fromGetPatientCoordinatesByOutpatientConsultationFilterDto(institutionId, filterDto);
		List<SanitaryRegionPatientMapCoordinatesBo> resultBo = getPatientCoordinatesByOutpatientConsultation.run(filter);
		List<SanitaryRegionPatientMapCoordinatesDto> result = sanitaryResponsibilityAreaMapper.toGetPatientCoordinatesByAddedInstitutionDtoList(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
