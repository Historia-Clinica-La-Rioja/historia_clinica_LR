package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.FetchConsultationsByDate;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.ConsultationDto;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.mapper.FetchConsultationsByDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Tag(name = "PublicApi Turnos", description = "endpoint of consultations and hierarchical units")
@RequestMapping("/public-api/reports/consultations")
@RestController
public class FetchConsultationsByDateController {

	private FetchConsultationsByDate fetchConsultations;

	@GetMapping
	public @ResponseBody List<ConsultationDto> fetchConsultationsByInstitution(
			@RequestParam(name = "dateFrom") String dateFrom,
			@RequestParam(name = "dateUntil") String dateUntil,
			@RequestParam(name = "institutionRefsetCode", required = false) String institutionRefsetCode,
			@RequestParam(name = "hierarchicalUnitId", required = false) Integer hierarchicalUnitId) {
		log.debug("Input parameters -> dateFrom {}, dateUntil{}, refsetCode {}, hierarchicalUnitId {}",
				dateFrom, dateUntil, institutionRefsetCode, hierarchicalUnitId);
		var consultations = fetchConsultations.run(dateFrom, dateUntil, institutionRefsetCode, hierarchicalUnitId);
		var result = consultations.stream()
				.map(FetchConsultationsByDateMapper::fromBo)
				.collect(Collectors.toList());
		log.debug("Consultations By Date {}", result);
		return result;
	}

}
