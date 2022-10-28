package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.application.fetchdocumentsinfobyactivity.FetchDocumentsInfoByActivity;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.DocumentInfoDto;

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

import ar.lamansys.sgh.publicapi.application.fetchactivitybyid.FetchActivityById;
import ar.lamansys.sgh.publicapi.application.fetchbedrelocationbyactivity.FetchBedRelocationByActivity;
import ar.lamansys.sgh.publicapi.application.fetchproceduresbyactivity.FetchProcedureByActivity;
import ar.lamansys.sgh.publicapi.application.fetchsuppliesbyactivity.FetchSuppliesByActivity;
import ar.lamansys.sgh.publicapi.application.processactivity.ProcessActivity;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.BedRelocationInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ProcedureInformationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SupplyInformationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.ActivitiesMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public-api/institution/refset/{refsetCode}/activities/{activityId}")
@Tag(name = "Public Api", description = "Public Api activity info")
public class ActivityInfoController {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityInfoController.class);
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final ActivitiesMapper activitiesMapper;
	private final FetchActivityById fetchActivityById;
	private final FetchProcedureByActivity fetchProcedureByActivity;
	private final ProcessActivity processActivity;
	private final FetchSuppliesByActivity fetchSuppliesByActivity;
	private final FetchBedRelocationByActivity fetchBedRelocationByActivity;
	private final FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity;

	public ActivityInfoController(ActivitiesMapper activitiesMapper,
								  FetchActivityById fetchActivityById,
								  FetchProcedureByActivity fetchProcedureByActivity,
								  ProcessActivity processActivity,
								  FetchSuppliesByActivity fetchSuppliesByActivity,
								  FetchBedRelocationByActivity fetchBedRelocationByActivity,
								  FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity) {
		this.activitiesMapper = activitiesMapper;
		this.fetchActivityById = fetchActivityById;
		this.fetchProcedureByActivity = fetchProcedureByActivity;
		this.processActivity = processActivity;
		this.fetchSuppliesByActivity = fetchSuppliesByActivity;
		this.fetchBedRelocationByActivity = fetchBedRelocationByActivity;
		this.fetchDocumentsInfoByActivity = fetchDocumentsInfoByActivity;
	}

	@GetMapping("")
	public ResponseEntity<AttentionInfoDto> getActivityByInstitution(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		LOG.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var attention = fetchActivityById.run(refsetCode, activityId);
		AttentionInfoDto result = (attention != null) ? activitiesMapper.mapToAttentionInfoDto(attention) : null;

		LOG.debug(OUTPUT, result);

		return ResponseEntity.ok().body(null);
	}

	@PutMapping("/process")
	public void markActivityAsProcessed(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		LOG.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);
		processActivity.run(refsetCode, activityId);
	}

	@GetMapping("/procedures")
	public ResponseEntity<List<ProcedureInformationDto>> getProceduresByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		LOG.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var procedureList = fetchProcedureByActivity.run(refsetCode, activityId);
		List<ProcedureInformationDto> result = procedureList
				.stream().map(activitiesMapper::mapTo).collect(Collectors.toList());

		LOG.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/supplies")
	public ResponseEntity<List<SupplyInformationDto>> getSuppliesByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		LOG.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var supplyList = fetchSuppliesByActivity.run(refsetCode, activityId);
		List<SupplyInformationDto> result = supplyList
				.stream().map(supply -> SupplyInformationDto.builder()
						.supplyType(supply.getSupplyType())
						.status(supply.getStatus())
						.snomed(activitiesMapper.mapTo(supply.getSnomedBo()))
						.administrationTime(supply.getAdministrationTime()).build())
				.collect(Collectors.toList());

		LOG.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/bedRelocations")
	public ResponseEntity<List<BedRelocationInfoDto>> getBedRelocationsByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		LOG.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);

		var bedRelocations = fetchBedRelocationByActivity.run(refsetCode, activityId);
		List<BedRelocationInfoDto> result = (bedRelocations != null) ? activitiesMapper.mapToBedRelocation(bedRelocations) : null;

		LOG.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/documents-info")
	@ResponseStatus(HttpStatus.OK)
	public List<DocumentInfoDto> getDocumentsInfoByActivity(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		LOG.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);
		var documents = fetchDocumentsInfoByActivity.run(refsetCode, activityId);
		List<DocumentInfoDto> result = (documents != null) ? activitiesMapper.mapToListDocumentInfoDto(documents) : null;
		LOG.debug(OUTPUT, result);
		return result;
	}
}