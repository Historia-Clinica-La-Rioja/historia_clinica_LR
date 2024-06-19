package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.activities.application.fetchdocumentsinfobyactivity.FetchDocumentsInfoByActivity;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.DocumentInfoDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.activities.application.fetchbedrelocationbyactivity.FetchBedRelocationByActivity;
import ar.lamansys.sgh.publicapi.activities.application.fetchproceduresbyactivity.FetchProcedureByActivity;
import ar.lamansys.sgh.publicapi.activities.application.fetchsuppliesbyactivity.FetchSuppliesByActivity;
import ar.lamansys.sgh.publicapi.activities.application.processactivity.ProcessActivity;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.BedRelocationInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ProcedureInformationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SupplyInformationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.ActivitiesMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/public-api/institution/refset/{refsetCode}/activities/{activityId}")
@Tag(name = "PublicApi Facturacion", description = "Public Api activity info")
public class ActivityInfoController {

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final ActivitiesMapper activitiesMapper;
	private final FetchProcedureByActivity fetchProcedureByActivity;
	private final FetchSuppliesByActivity fetchSuppliesByActivity;
	private final FetchBedRelocationByActivity fetchBedRelocationByActivity;
	private final FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity;

	@GetMapping("/procedures")
	public ResponseEntity<List<ProcedureInformationDto>> getProceduresByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		log.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var procedureList = fetchProcedureByActivity.run(refsetCode, activityId);
		List<ProcedureInformationDto> result = procedureList
				.stream().map(activitiesMapper::mapTo).collect(Collectors.toList());

		log.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/supplies")
	public ResponseEntity<List<SupplyInformationDto>> getSuppliesByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		log.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var supplyList = fetchSuppliesByActivity.run(refsetCode, activityId);
		List<SupplyInformationDto> result = supplyList
				.stream().map(activitiesMapper::mapTo).collect(Collectors.toList());

		log.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/bedRelocations")
	public ResponseEntity<List<BedRelocationInfoDto>> getBedRelocationsByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		log.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var bedRelocations = fetchBedRelocationByActivity.run(refsetCode, activityId);
		List<BedRelocationInfoDto> result = (bedRelocations != null) ? activitiesMapper.mapToBedRelocation(bedRelocations) : null;

		log.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/documents-info")
	@ResponseStatus(HttpStatus.OK)
	public List<DocumentInfoDto> getDocumentsInfoByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		log.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);
		var documents = fetchDocumentsInfoByActivity.run(refsetCode, activityId);
		List<DocumentInfoDto> result = (documents != null) ? activitiesMapper.mapToListDocumentInfoDto(documents) : null;
		log.debug(OUTPUT, result);
		return result;
	}
}