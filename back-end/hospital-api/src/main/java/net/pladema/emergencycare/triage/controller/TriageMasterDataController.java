package net.pladema.emergencycare.triage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.controller.mapper.TriageMasterDataMapper;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.emergencycare.triage.service.domain.enums.EBodyTemperature;
import net.pladema.emergencycare.triage.service.domain.enums.EMuscleHypertonia;
import net.pladema.emergencycare.triage.service.domain.enums.EPerfusion;
import net.pladema.emergencycare.triage.service.domain.enums.ERespiratoryRetraction;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emergency-care/triage/masterdata")
@Tag(name = "Triage master data", description = "Triage master data")
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
    public ResponseEntity<List<MasterDataDto>> getBodyTemperature() {
        LOG.debug("{}", "All body temperature");
        List<EBodyTemperature> data = triageMasterDataService.getBodyTemperature();
        return ResponseEntity.ok().body(EnumWriter.writeList(data));
    }

    @GetMapping("/muscleHypertonia")
    public ResponseEntity<List<MasterDataDto>> getMuscleHypertonia() {
        LOG.debug("{}", "All muscle hypertonia");
        List<EMuscleHypertonia> data = triageMasterDataService.getMuscleHypertonia();
        return ResponseEntity.ok().body(EnumWriter.writeList(data));
    }

    @GetMapping("/respiratoryRetraction")
    public ResponseEntity<List<MasterDataDto>> getRespiratoryRetraction() {
        LOG.debug("{}", "All muscle respiratory retraction");
        List<ERespiratoryRetraction> data = triageMasterDataService.getRespiratoryRetraction();
        return ResponseEntity.ok().body(EnumWriter.writeList(data));
    }

    @GetMapping("/perfusion")
    public ResponseEntity<List<MasterDataDto>> getPerfusion() {
        LOG.debug("{}", "All muscle respiratory retraction");
        List<EPerfusion> data = triageMasterDataService.getPerfusion();
        return ResponseEntity.ok().body(EnumWriter.writeList(data));
    }
}
