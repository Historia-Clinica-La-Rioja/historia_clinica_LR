package net.pladema.emergencycare.triage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.controller.dto.TriageDetailsDto;
import net.pladema.emergencycare.triage.controller.mapper.TriageMasterDataMapper;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.emergencycare.triage.service.domain.enums.EBodyTemperature;
import net.pladema.emergencycare.triage.service.domain.enums.EMuscleHypertonia;
import net.pladema.emergencycare.triage.service.domain.enums.EPerfusion;
import net.pladema.emergencycare.triage.service.domain.enums.ERespiratoryRetraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/emergency-care/triage/masterdata")
@Api(value = "Emergency care Triage Master Data", tags = { "Triage Master Data" })
public class TriageMasterDataController {

    private static final Logger LOG = LoggerFactory.getLogger(TriageMasterDataController.class);

    private final TriageMasterDataService triageMasterDataService;

    private final TriageMasterDataMapper triageMasterDataMapper;

    private final ObjectMapper jackson;

    public TriageMasterDataController(TriageMasterDataService triageMasterDataService,
                                      TriageMasterDataMapper triageMasterDataMapper,
                                      ObjectMapper jackson){
        super();
        this.triageMasterDataService = triageMasterDataService;
        this.triageMasterDataMapper = triageMasterDataMapper;
        this.jackson=jackson;
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
        List<EBodyTemperature> data = triageMasterDataService.getBodyTemperature();
        return ResponseEntity.ok().body(get(data));
    }

    @GetMapping("/muscleHypertonia")
    public ResponseEntity<List<TriageDetailsDto>> getMuscleHypertonia() {
        LOG.debug("{}", "All muscle hypertonia");
        List<EMuscleHypertonia> data = triageMasterDataService.getMuscleHypertonia();
        return ResponseEntity.ok().body(get(data));
    }

    @GetMapping("/respiratoryRetraction")
    public ResponseEntity<List<TriageDetailsDto>> getRespiratoryRetraction() {
        LOG.debug("{}", "All muscle respiratory retraction");
        List<ERespiratoryRetraction> data = triageMasterDataService.getRespiratoryRetraction();
        return ResponseEntity.ok().body(get(data));
    }

    @GetMapping("/perfusion")
    public ResponseEntity<List<TriageDetailsDto>> getPerfusion() {
        LOG.debug("{}", "All muscle respiratory retraction");
        List<EPerfusion> data = triageMasterDataService.getPerfusion();
        return ResponseEntity.ok().body(get(data));
    }

    private List<TriageDetailsDto> get(List<? extends Enum> list){
        List<TriageDetailsDto> result = new ArrayList<>();
        list.forEach(e -> {
            try {
                result.add(jackson.readValue(jackson.writeValueAsString(e), TriageDetailsDto.class));
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });
        return result;
    }
}
