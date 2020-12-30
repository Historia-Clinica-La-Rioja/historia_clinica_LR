package net.pladema.emergencycare.triage.controller;

import io.swagger.annotations.Api;
import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.controller.dto.TriageDetailsDto;
import net.pladema.emergencycare.triage.controller.mapper.TriageMasterDataMapper;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/emergency-care/triage/masterdata")
@Api(value = "Emergency care Triage Master Data", tags = { "Triage Master Data" })
public class TriageMasterDataController {

    private static final Logger LOG = LoggerFactory.getLogger(TriageMasterDataController.class);

    private final TriageMasterDataService triageMasterDataService;

    private final TriageMasterDataMapper triageMasterDataMapper;

    public TriageMasterDataController(TriageMasterDataService triageMasterDataService,
                                      TriageMasterDataMapper triageMasterDataMapper){
        super();
        this.triageMasterDataService = triageMasterDataService;
        this.triageMasterDataMapper = triageMasterDataMapper;
    }

    @GetMapping("/category")
    public ResponseEntity<List<TriageCategoryDto>> getCategories() {
        LOG.debug("{}", "All triage categories");
        List<TriageCategoryBo> triageCategoryBos = triageMasterDataService.getCategories();
        List<TriageCategoryDto> result = triageMasterDataMapper.toTriageCategoryDtoList(triageCategoryBos);
        LOG.debug("Output size = {}", result.size());
        LOG.trace("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/bodyTemperature")
    public ResponseEntity<List<TriageDetailsDto>> getBodyTemperature() {
        LOG.debug("{}", "All body temperature");
        return ResponseEntity.ok().body(Collections.singletonList(new TriageDetailsDto()));
    }

    @GetMapping("/muscleHypertonia")
    public ResponseEntity<List<TriageDetailsDto>> getMuscleHypertonia() {
        LOG.debug("{}", "All muscle hypertonia");
        return ResponseEntity.ok().body(Collections.singletonList(new TriageDetailsDto()));
    }

    @GetMapping("/respiratoryRetraction")
    public ResponseEntity<List<TriageDetailsDto>> getRespiratoryRetraction() {
        LOG.debug("{}", "All muscle respiratory retraction");
        return ResponseEntity.ok().body(Collections.singletonList(new TriageDetailsDto()));
    }

    @GetMapping("/perfusion")
    public ResponseEntity<List<TriageDetailsDto>> getPerfusion() {
        LOG.debug("{}", "All muscle respiratory retraction");
        return ResponseEntity.ok().body(Collections.singletonList(new TriageDetailsDto()));
    }
}
