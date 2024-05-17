package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.apisumar.application.fetchconsultationsbyinstitutionid.FetchConsultationsByInstitutionId;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@Tag(name = "PublicApi Sumar", description = "Public Api Sumar Access")
@RequestMapping("/public-api/institution/{institutionId}/sumar-consultation-details")
public class SumarApiController {
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final FetchConsultationsByInstitutionId fetchConsultationsByInstitutionId;

	private final ConsultationMapper consultationMapper;

	public SumarApiController(
			FetchConsultationsByInstitutionId fetchConsultationsByInstitutionId,
			ConsultationMapper consultationMapper
	) {
		this.fetchConsultationsByInstitutionId = fetchConsultationsByInstitutionId;
		this.consultationMapper = consultationMapper;
	}

	@GetMapping
	public ResponseEntity<List<ConsultationDetailDataDto>> consultationRequest(
			@PathVariable("institutionId") Integer institutionId
	) throws BadConsultationFormatException, ConsultationNotFoundException {
		log.debug(INPUT + "institutionId {}", institutionId);

		try {
			var consultations = fetchConsultationsByInstitutionId.run(institutionId);
			List<ConsultationDetailDataDto> result = (consultations != null) ? consultationMapper.mapToConsultations(consultations) : null;

			log.debug(OUTPUT, result);

			return ResponseEntity.ok().body(result);
		} catch (RuntimeException e) {
			throw new ConsultationRequestException(e.getMessage(), e);
		}

	}

}
