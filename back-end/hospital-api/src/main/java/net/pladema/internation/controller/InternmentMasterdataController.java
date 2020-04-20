package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.repository.masterdata.entity.*;
import net.pladema.internation.repository.projections.InternmentMasterDataProjection;
import net.pladema.internation.service.InternmentMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/internments/masterdata")
@Api(value = "Internments Master Data", tags = { "Internments Master Data" })
public class InternmentMasterdataController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final InternmentMasterDataService internmentMasterDataService;

    public InternmentMasterdataController(InternmentMasterDataService internmentMasterDataService){
        super();
        this.internmentMasterDataService = internmentMasterDataService;
    }

    @GetMapping(value = "/allergy/category")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getAllergyCategory(){
        LOG.debug("{}", "All allergy intolerance category");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(AllergyIntoleranceCategory.class));
    }

    @GetMapping(value = "/allergy/clinical")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getAllergyClinical(){
        LOG.debug("{}", "All allergy intolerance clinical status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(AllergyIntoleranceClinicalStatus.class));
    }

    @GetMapping(value = "/allergy/verification")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getAllergyVerification(){
        LOG.debug("{}", "All allergy intolerance verification status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(AllergyIntoleranceVerificationStatus.class));
    }

    @GetMapping(value = "/health/clinical")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getHealthClinical(){
        LOG.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionClinicalStatus.class));
    }

    @GetMapping(value = "/health/verification")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getHealthVerification(){
        LOG.debug("{}", "All health condition verification status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionVerificationStatus.class));
    }

    @GetMapping(value = "/health/problem")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getHealthProblem(){
        LOG.debug("{}", "All health condition problem type");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionProblemType.class));
    }

    @GetMapping(value = "/medication")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getHealthMedication(){
        LOG.debug("{}", "All medication statement status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(MedicationStatementStatus.class));
    }

    @GetMapping(value = "/inmunization")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getInmunization(){
        LOG.debug("{}", "All inmunization status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(InmunizationStatus.class));
    }

    @GetMapping(value = "/document/status")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getDocumentStatus(){
        LOG.debug("{}", "All document status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(DocumentStatus.class));
    }

    @GetMapping(value = "/document/type")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getDocumentType(){
        LOG.debug("{}", "All document type");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(DocumentType.class));
    }

    @GetMapping(value = "/observation")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getObservation(){
        LOG.debug("{}", "All observation");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ObservationStatus.class));
    }

    @GetMapping(value = "/episode")
    ResponseEntity<Collection<InternmentMasterDataProjection>> getInternmentEpisode(){
        LOG.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(InternmentEpisodeStatus.class));
    }
}
