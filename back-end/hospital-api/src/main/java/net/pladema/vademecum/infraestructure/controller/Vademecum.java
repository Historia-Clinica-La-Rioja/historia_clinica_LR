package net.pladema.vademecum.infraestructure.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.vademecum.application.FetchVademecum;

import net.pladema.vademecum.domain.VademecumResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vademecum")
public class Vademecum {

	private final Logger logger;

	private final FetchVademecum fetchVademecum;

	public Vademecum(FetchVademecum fetchVademecum) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.fetchVademecum = fetchVademecum;
	}

	@GetMapping(value = "/concepts")
	public VademecumResponseDto getConcepts(@RequestParam(name = "term") String term,
									@RequestParam(name = "ecl") String ecl,
									@RequestParam(name = "institutionId") Integer institutionId)
	{
		logger.debug("Output -> {}", term);
		List<SnomedDto> vademecumConcepts = this.fetchVademecum.getConcepts(term, ecl, institutionId)
				.stream()
				.map(s -> new SnomedDto(s.getSctid(), s.getPt())).collect(Collectors.toList());
		Long total = this.fetchVademecum.getTotalConcepts(term, ecl, institutionId);
		return new VademecumResponseDto(vademecumConcepts, total);
	}
}
