package net.pladema.clinichistory.requests.servicerequests.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "Service request work list", description = "Service Request Study Order List")
@RestController
@RequestMapping("/institutions/{institutionId}/service-request-work-list")
public class ServiceRequestWorkListController {

	private StudyWorkListService studyWorkListService;

	@GetMapping
	public ResponseEntity<List<Object>> getList(@PathVariable(name = "institutionId") Integer institutionId,
												@RequestParam(value = "categories", required = true) List<String> categories){
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<Object> result = studyWorkListService.execute();
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

}
