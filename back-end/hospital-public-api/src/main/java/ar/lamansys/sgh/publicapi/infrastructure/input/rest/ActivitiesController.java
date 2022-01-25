package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter.ActivitySearchFilter;
import ar.lamansys.sgh.publicapi.application.fetchactivitiesbyfilter.FetchActivitiesByFilter;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.ActivitiesMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public-api")
@Tag(name = "Public Api", description = "Public Api Activities")
public class ActivitiesController {

    private static final Logger LOG = LoggerFactory.getLogger(ActivitiesController.class);
    private static final String OUTPUT = "Output -> {}";
    private static final String INPUT = "Input data -> ";

    private final ActivitiesMapper activitiesMapper;
    private final FetchActivitiesByFilter fetchActivitiesByFilter;
    private final LocalDateMapper localDateMapper;

    public ActivitiesController(ActivitiesMapper activitiesMapper, FetchActivitiesByFilter fetchActivitiesByFilter, LocalDateMapper localDateMapper) {
        this.activitiesMapper = activitiesMapper;
        this.fetchActivitiesByFilter = fetchActivitiesByFilter;
        this.localDateMapper = localDateMapper;
    }

    @GetMapping("/institution/refset/{refsetCode}/province-code/{provinceCode}/activities")
    public ResponseEntity<List<AttentionInfoDto>> getActivitiesByInstitution(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("reprocessing") Boolean reprocessing
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode {}, from {}, to {}, reprocessing {}", refsetCode, provinceCode, from, to, reprocessing);

        ActivitySearchFilter activitySearchFilter = new ActivitySearchFilter(
                refsetCode, provinceCode, null, localDateMapper.fromStringToLocalDate(from), localDateMapper.fromStringToLocalDate(to), reprocessing, null);
        List<AttentionInfoDto> resultBo = activitiesMapper.mapTo(fetchActivitiesByFilter.run(activitySearchFilter));

        LOG.debug(OUTPUT, resultBo);

        return ResponseEntity.ok().body(resultBo);
    }

    @GetMapping("/institution/refset/{refsetCode}/province-code/{provinceCode}/patient/{identificationNumber}/activities")
    public ResponseEntity<List<AttentionInfoDto>> getActivitiesByInstitutionAndPatient(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("identificationNumber") String identificationNumber,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("reprocessing") Boolean reprocessing
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, identificationNumber{}, from {}, to {}, reprocessing {}",
                refsetCode, provinceCode, identificationNumber, from, to, reprocessing);

        ActivitySearchFilter activitySearchFilter = new ActivitySearchFilter(
                refsetCode, provinceCode, identificationNumber, localDateMapper.fromStringToLocalDate(from), localDateMapper.fromStringToLocalDate(to), reprocessing, null);
        List<AttentionInfoDto> resultBo = activitiesMapper.mapTo(fetchActivitiesByFilter.run(activitySearchFilter));

        LOG.debug(OUTPUT, resultBo);

        return ResponseEntity.ok().body(resultBo);
    }

    @GetMapping("/institution/refset/{refsetCode}/province-code/{provinceCode}/coverage/{coverageCuit}/activities")
    public ResponseEntity<List<AttentionInfoDto>> getActivitiesByInstitutionAndCoverage(
            @PathVariable("refsetCode") String refsetCode,
            @PathVariable("provinceCode") String provinceCode,
            @PathVariable("coverageCuit") String coverageCuit,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("reprocessing") Boolean reprocessing
    ) {
        LOG.debug(INPUT + "refsetCode {}, provinceCode{}, coverageCuit{}, from {}, to {}, reprocessing {}",
                refsetCode, provinceCode, coverageCuit, from, to, reprocessing);

        ActivitySearchFilter activitySearchFilter = new ActivitySearchFilter(
                refsetCode, provinceCode, null, localDateMapper.fromStringToLocalDate(from), localDateMapper.fromStringToLocalDate(to), reprocessing, coverageCuit);
        List<AttentionInfoDto> resultBo = activitiesMapper.mapTo(fetchActivitiesByFilter.run(activitySearchFilter));

        LOG.debug(OUTPUT, resultBo);

        return ResponseEntity.ok().body(resultBo);
    }
}
