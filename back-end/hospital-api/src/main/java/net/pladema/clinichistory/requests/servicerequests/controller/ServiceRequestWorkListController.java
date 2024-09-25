package net.pladema.clinichistory.requests.servicerequests.controller;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderWorkListDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;
import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Tag(name = "Service request work list", description = "Service Request Study Order List")
@RestController
@RequestMapping("/institutions/{institutionId}/service-request-work-list")
public class ServiceRequestWorkListController {

	private StudyWorkListService studyWorkListService;
	private StudyMapper studyMapper;

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
	public ResponseEntity<List<StudyOrderWorkListDto>> getList(@PathVariable(name = "institutionId") Integer institutionId,
															   @RequestParam(value = "categories") List<String> categories){

		log.debug("Input parameters -> institutionId {}", institutionId);

		if (!AppFeature.HABILITAR_LISTA_DE_TRABAJO_EN_DESARROLLO.isActive()) {
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		}

		List<StudyOrderWorkListBo> resultService = studyWorkListService.execute(institutionId, categories);
		List<StudyOrderWorkListDto> result = resultService
				.stream()
				.map(studyOrderWorkListBo -> studyMapper.toStudyOrderWorkListDto(studyOrderWorkListBo))
				.collect(Collectors.toList());

		log.debug("Output -> {}", result);

		return ResponseEntity.ok(result);
	}

}
