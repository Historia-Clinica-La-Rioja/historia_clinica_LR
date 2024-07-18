package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.apisumar.application.fetchconsultationsbysisacode.FetchConsultationsBySisaCode;
import ar.lamansys.sgh.publicapi.apisumar.application.fetchimmunizationsbysisacode.FetchImmunizationsBySisaCode;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.BadConsultationFormatException;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.ConsultationNotFoundException;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.ConsultationRequestException;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto.ConsultationDetailDataDto;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto.ImmunizationsDetailDto;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.mapper.ConsultationMapper;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.mapper.ImmunizationsMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@Validated
@Tag(name = "PublicApi Sumar", description = "Public Api Sumar Access")
@RequestMapping("/public-api/institution/{sisaCode}")
public class SumarApiController {
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final FetchConsultationsBySisaCode fetchConsultationsBySisaCode;

	private final FetchImmunizationsBySisaCode fetchImmunizationsBySisaCode;

	private final ConsultationMapper consultationMapper;

	private final ImmunizationsMapper immunizationsMapper;

	private final LocalDateMapper localDateMapper;

	public SumarApiController(
			FetchConsultationsBySisaCode fetchConsultationsBySisaCode,
			FetchImmunizationsBySisaCode fetchImmunizationsBySisaCode,
			ConsultationMapper consultationMapper,
			ImmunizationsMapper immunizationsMapper,
			LocalDateMapper localDateMapper
	) {
		this.fetchConsultationsBySisaCode = fetchConsultationsBySisaCode;
		this.fetchImmunizationsBySisaCode = fetchImmunizationsBySisaCode;
		this.consultationMapper = consultationMapper;
		this.immunizationsMapper = immunizationsMapper;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping("/sumar-consultation-details")
	public ResponseEntity<List<ConsultationDetailDataDto>> consultationRequest(
			@PathVariable("sisaCode") String sisaCode,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate
	) throws BadConsultationFormatException, ConsultationNotFoundException {
		log.debug(INPUT + "sisaCode {}", sisaCode);

		try {
			LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
			LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

			var consultations = fetchConsultationsBySisaCode.run(sisaCode, startDate, endDate);
			List<ConsultationDetailDataDto> result = (consultations != null) ? consultationMapper.mapToConsultations(consultations) : null;

			log.debug(OUTPUT, result);

			return ResponseEntity.ok().body(result);
		} catch (RuntimeException e) {
			throw new ConsultationRequestException(e.getMessage(), e);
		}

	}

	@GetMapping("/sumar-immunization-details")
	public ResponseEntity<List<ImmunizationsDetailDto>> immunizationsRequest(
			@PathVariable("sisaCode") String sisaCode,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate
	) throws BadConsultationFormatException, ConsultationNotFoundException {
		log.debug(INPUT + "sisaCode {}", sisaCode);

		try {
			LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
			LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

			var immunizations = fetchImmunizationsBySisaCode.run(sisaCode, startDate, endDate);
			List<ImmunizationsDetailDto> result = (immunizations != null) ? immunizationsMapper.mapToImmunizations(immunizations) : null;

			log.debug(OUTPUT, result);

			return ResponseEntity.ok().body(result);
		} catch (RuntimeException e) {
			throw new ConsultationRequestException(e.getMessage(), e);
		}
	}

}
