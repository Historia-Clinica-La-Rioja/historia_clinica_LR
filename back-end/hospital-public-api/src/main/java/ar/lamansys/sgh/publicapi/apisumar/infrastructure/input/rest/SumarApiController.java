package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.apisumar.application.fetchconsultationsbysisacode.FetchConsultationsBySisaCode;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.BadConsultationFormatException;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.ConsultationNotFoundException;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.ConsultationRequestException;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto.ConsultationDetailDataDto;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.mapper.ConsultationMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Validated
@Tag(name = "PublicApi Sumar", description = "Public Api Sumar Access")
@RequestMapping("/public-api/institution/{sisaCode}/sumar-consultation-details")
public class SumarApiController {
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final FetchConsultationsBySisaCode fetchConsultationsBySisaCode;

	private final ConsultationMapper consultationMapper;

	public SumarApiController(
			FetchConsultationsBySisaCode fetchConsultationsBySisaCode,
			ConsultationMapper consultationMapper
	) {
		this.fetchConsultationsBySisaCode = fetchConsultationsBySisaCode;
		this.consultationMapper = consultationMapper;
	}

	@GetMapping
	public ResponseEntity<List<ConsultationDetailDataDto>> consultationRequest(
			@PathVariable("sisaCode") String sisaCode
	) throws BadConsultationFormatException, ConsultationNotFoundException {
		log.debug(INPUT + "sisaCode {}", sisaCode);

		try {
			var consultations = fetchConsultationsBySisaCode.run(sisaCode);
			List<ConsultationDetailDataDto> result = (consultations != null) ? consultationMapper.mapToConsultations(consultations) : null;

			log.debug(OUTPUT, result);

			return ResponseEntity.ok().body(result);
		} catch (RuntimeException e) {
			throw new ConsultationRequestException(e.getMessage(), e);
		}

	}

}
