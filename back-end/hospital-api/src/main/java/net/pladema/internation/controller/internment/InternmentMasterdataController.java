package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.internation.repository.documents.entity.DischargeType;
import net.pladema.internation.repository.masterdata.entity.*;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import net.pladema.internation.service.internment.InternmentMasterDataService;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
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

    private static final Logger LOG = LoggerFactory.getLogger(InternmentMasterdataController.class);

    private final InternmentMasterDataService internmentMasterDataService;

    public InternmentMasterdataController(InternmentMasterDataService internmentMasterDataService){
        super();
        this.internmentMasterDataService = internmentMasterDataService;
    }

    @GetMapping(value = "/allergy/category")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyCategory(){
        LOG.debug("{}", "All allergy intolerance category");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(AllergyIntoleranceCategory.class));
    }

    @GetMapping(value = "/allergy/clinical")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyClinical(){
        LOG.debug("{}", "All allergy intolerance clinical status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(AllergyIntoleranceClinicalStatus.class));
    }

    @GetMapping(value = "/allergy/verification")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyVerification(){
        LOG.debug("{}", "All allergy intolerance verification status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(AllergyIntoleranceVerificationStatus.class));
    }

    @GetMapping(value = "/health/clinical")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthClinical(){
        LOG.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionClinicalStatus.class));
    }

    @GetMapping(value = "/health/clinical/down")
    public ResponseEntity<Collection<MasterDataProjection>> getDownHealthClinical(){
        LOG.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionClinicalStatus.class,
                ConditionClinicalStatus.downState()));
    }


    @GetMapping(value = "/health/verification")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthVerification(){
        LOG.debug("{}", "All health condition verification status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionVerificationStatus.class));
    }

    @GetMapping(value = "/health/verification/down")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthVerificationDown(){
        LOG.debug("{}", "All health condition verification status down");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(
                ConditionVerificationStatus.class,
                ConditionVerificationStatus.downState()));
    }

    @GetMapping(value = "/health/problem")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthProblem(){
        LOG.debug("{}", "All health condition problem type");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ConditionProblemType.class));
    }

    @GetMapping(value = "/medication")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthMedication(){
        LOG.debug("{}", "All medication statement status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(MedicationStatementStatus.class));
    }

    @GetMapping(value = "/inmunization")
    public ResponseEntity<Collection<MasterDataProjection>> getInmunization(){
        LOG.debug("{}", "All inmunization status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(InmunizationStatus.class));
    }

    @GetMapping(value = "/document/status")
    public ResponseEntity<Collection<MasterDataProjection>> getDocumentStatus(){
        LOG.debug("{}", "All document status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(DocumentStatus.class));
    }

    @GetMapping(value = "/document/type")
    public ResponseEntity<Collection<MasterDataProjection>> getDocumentType(){
        LOG.debug("{}", "All document type");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(DocumentType.class));
    }

    @GetMapping(value = "/observation")
    public ResponseEntity<Collection<MasterDataProjection>> getObservation(){
        LOG.debug("{}", "All observation");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ObservationStatus.class));
    }

    @GetMapping(value = "/episode")
    public ResponseEntity<Collection<MasterDataProjection>> getInternmentEpisode(){
        LOG.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(InternmentEpisodeStatus.class));
    }

    @GetMapping(value = "/bloodtypes")
    public ResponseEntity<Collection<MasterDataProjection>> getBloodTypes(){
        LOG.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(BloodType.class));
    }
    
    @GetMapping(value = "/clinical/specialty")
    public ResponseEntity<Collection<MasterDataProjection>> getClinicalSpecialty(){
        LOG.debug("{}", "All internment clinical specialty");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(ClinicalSpecialty.class));
    }
    
    @GetMapping(value = "/discharge/type")
    public ResponseEntity<Collection<MasterDataProjection>> getDischargeType(){
        LOG.debug("All internment discharge types ");
        return ResponseEntity.ok().body(internmentMasterDataService.findAll(DischargeType.class));
    }
    
    
}
