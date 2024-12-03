package net.pladema.clinichistory.requests.servicerequests.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderWorkListDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderWorkListFilterDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyOrderWorkListFilterMapper;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListFilterBo;
import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	private StudyOrderWorkListFilterMapper filterMapper;
	private ObjectMapper objectMapper;

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
	public ResponseEntity<PageDto<StudyOrderWorkListDto>> getList(@PathVariable(name = "institutionId") Integer institutionId,
																  @RequestParam(name = "pageNumber") Integer pageNumber,
																  @RequestParam(name = "pageSize") Integer pageSize,
																  @RequestParam(name = "filter") String filterData) throws JsonProcessingException {

		log.debug("Input parameters -> institutionId {}, pageNumber {}, pageSize {}", institutionId, pageNumber, pageSize);

		if (!AppFeature.HABILITAR_LISTA_DE_TRABAJO_EN_DESARROLLO.isActive()) {
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		}

		StudyOrderWorkListFilterBo filter = filterMapper.fromStudyOrderWorkListFilterDto(objectMapper.readValue(filterData, StudyOrderWorkListFilterDto.class));

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<StudyOrderWorkListBo> resultService = studyWorkListService.execute(institutionId, null, pageable);
		Page<StudyOrderWorkListDto> result = resultService.map(studyOrderWorkListBo -> studyMapper.toStudyOrderWorkListDto(studyOrderWorkListBo));

		log.debug("Output -> {}", result);

		return ResponseEntity.ok(PageDto.fromPage(result));
	}

}
