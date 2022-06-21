package net.pladema.sgx.healthinsurance.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.pladema.patient.controller.dto.ARTCoverageDto;
import net.pladema.patient.service.domain.ARTCoverageBo;
import net.pladema.sgx.healthinsurance.service.ARTCoverageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ART")
public class ARTCoverageController {

	private static final Logger LOG = LoggerFactory.getLogger(ARTCoverageController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ARTCoverageService artCoverageService;

	@GetMapping
	public ResponseEntity<Collection<ARTCoverageDto>> getAll() {
		LOG.debug("{}", "All health insurance");
		Collection<ARTCoverageBo> data = artCoverageService.getAll();
		Collection<ARTCoverageDto> result = data.stream().map(mc -> new ARTCoverageDto(mc.getId(), mc.getName(), mc.getCuit(), mc.getType())).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

}
