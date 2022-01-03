package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public-api/institution/refset/{refsetCode}/province-code/{provinceCode}/activities/{activityId}")
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

    public ActivityInfoController(ActivitiesMapper activitiesMapper, FetchActivityById fetchActivityById, FetchProcedureByActivity fetchProcedureByActivity, ProcessActivity processActivity, FetchSuppliesByActivity fetchSuppliesByActivity, FetchBedRelocationByActivity fetchBedRelocationByActivity) {
        this.activitiesMapper = activitiesMapper;
        this.fetchActivityById = fetchActivityById;
        this.fetchProcedureByActivity = fetchProcedureByActivity;
        this.processActivity = processActivity;
        this.fetchSuppliesByActivity = fetchSuppliesByActivity;
        this.fetchBedRelocationByActivity = fetchBedRelocationByActivity;
    }

    @GetMapping("")
    public ResponseEntity<AttentionInfoDto> getActivityByInstitution(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("activityId") Long activityId
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, activityId{}", refsetCode, provinceCode, activityId);

        var attention = fetchActivityById.run(refsetCode, provinceCode, activityId);
        AttentionInfoDto result = (attention != null) ? activitiesMapper.mapTo(attention) : null;

        LOG.debug(OUTPUT, result);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/process")
    public void markActivityAsProcessed(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("activityId") Long activityId
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, activityId{}", refsetCode, provinceCode, activityId);
        processActivity.run(refsetCode, provinceCode, activityId);
    }

    @GetMapping("/procedures")
    public ResponseEntity<List<ProcedureInformationDto>> getProceduresByActivity(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("activityId") Long activityId
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, activityId{}", refsetCode, provinceCode, activityId);

        var snomedList = fetchProcedureByActivity.run(refsetCode, provinceCode, activityId);
        List<ProcedureInformationDto> result = activitiesMapper.mapToSnomed(snomedList)
                .stream().map(snomed -> ProcedureInformationDto.builder()
                .snomed(snomed).build()).collect(Collectors.toList());

        LOG.debug(OUTPUT, result);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/supplies")
    public ResponseEntity<List<SupplyInformationDto>> getSuppliesByActivity(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("activityId") Long activityId
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, activityId{}", refsetCode, provinceCode, activityId);

        var snomedList = fetchSuppliesByActivity.run(refsetCode, provinceCode, activityId);
        List<SupplyInformationDto> result = activitiesMapper.mapToSnomed(snomedList)
                .stream().map(snomed -> SupplyInformationDto.builder()
                .snomed(snomed).build())
                .collect(Collectors.toList());

        LOG.debug(OUTPUT, result);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/bedRelocations")
    public ResponseEntity<List<BedRelocationInfoDto>> getBedRelocationsByActivity(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("activityId") Long activityId
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, activityId{}", refsetCode, provinceCode, activityId);

        var snomed = fetchBedRelocationByActivity.run(refsetCode, provinceCode, activityId);
        List<BedRelocationInfoDto> result = (snomed != null) ? activitiesMapper.mapToBedRelocation(snomed) : null;

        LOG.debug(OUTPUT, result);

        return ResponseEntity.ok().body(result);
    }
}
