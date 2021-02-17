package net.pladema.clinichistory.hospitalization.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceCategory;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceVerificationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.BloodType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.InmunizationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.InternmentEpisodeStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ObservationStatus;
import net.pladema.emergencycare.controller.DischargeTypeMasterDataExternalService;
import net.pladema.sgx.masterdata.dto.MasterDataDto;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import net.pladema.sgx.masterdata.service.MasterDataService;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.ClinicalSpecialtyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internments/masterdata")
@Api(value = "Internments Master Data", tags = { "Internments Master Data" })
public class InternmentMasterdataController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentMasterdataController.class);

    private final MasterDataService masterDataService;

    private final DischargeTypeMasterDataExternalService dischargeExternalService;

    public InternmentMasterdataController(MasterDataService masterDataService,
                                          DischargeTypeMasterDataExternalService dischargeExternalService){
        super();
        this.masterDataService = masterDataService;
        this.dischargeExternalService = dischargeExternalService;
    }

    @GetMapping(value = "/allergy/category")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyCategory(){
        LOG.debug("{}", "All allergy intolerance category");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceCategory.class));
    }

    @GetMapping(value = "/allergy/clinical")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyClinical(){
        LOG.debug("{}", "All allergy intolerance clinical status");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceClinicalStatus.class));
    }

    @GetMapping(value = "/allergy/verification")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyVerification(){
        LOG.debug("{}", "All allergy intolerance verification status");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceVerificationStatus.class));
    }

    @GetMapping(value = "/health/clinical")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthClinical(){
        LOG.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(masterDataService.findAll(ConditionClinicalStatus.class));
    }

    @GetMapping(value = "/health/clinical/down")
    public ResponseEntity<Collection<MasterDataProjection>> getDownHealthClinical(){
        LOG.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(masterDataService.findAll(ConditionClinicalStatus.class,
                ConditionClinicalStatus.downState()));
    }


    @GetMapping(value = "/health/verification")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthVerification(){
        LOG.debug("{}", "All health condition verification status");
        return ResponseEntity.ok().body(masterDataService.findAll(ConditionVerificationStatus.class));
    }

    @GetMapping(value = "/health/verification/down")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthVerificationDown(){
        LOG.debug("{}", "All health condition verification status down");
        return ResponseEntity.ok().body(masterDataService.findAll(
                ConditionVerificationStatus.class,
                ConditionVerificationStatus.downState()));
    }

    @GetMapping(value = "/medication")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthMedication(){
        LOG.debug("{}", "All medication statement status");
        return ResponseEntity.ok().body(masterDataService.findAll(MedicationStatementStatus.class));
    }

    @GetMapping(value = "/inmunization")
    public ResponseEntity<Collection<MasterDataProjection>> getInmunization(){
        LOG.debug("{}", "All inmunization status");
        return ResponseEntity.ok().body(masterDataService.findAll(InmunizationStatus.class));
    }

    @GetMapping(value = "/document/status")
    public ResponseEntity<Collection<MasterDataProjection>> getDocumentStatus(){
        LOG.debug("{}", "All document status");
        return ResponseEntity.ok().body(masterDataService.findAll(DocumentStatus.class));
    }

    @GetMapping(value = "/document/type")
    public ResponseEntity<Collection<MasterDataProjection>> getDocumentType(){
        LOG.debug("{}", "All document type");
        return ResponseEntity.ok().body(masterDataService.findAll(DocumentType.class));
    }

    @GetMapping(value = "/observation")
    public ResponseEntity<Collection<MasterDataProjection>> getObservation(){
        LOG.debug("{}", "All observation");
        return ResponseEntity.ok().body(masterDataService.findAll(ObservationStatus.class));
    }

    @GetMapping(value = "/episode")
    public ResponseEntity<Collection<MasterDataProjection>> getInternmentEpisode(){
        LOG.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(masterDataService.findAll(InternmentEpisodeStatus.class));
    }

    @GetMapping(value = "/bloodtypes")
    public ResponseEntity<Collection<MasterDataProjection>> getBloodTypes(){
        LOG.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(masterDataService.findAll(BloodType.class));
    }
    
	@GetMapping(value = "/clinical/specialty")
	public ResponseEntity<Collection<MasterDataProjection>> getClinicalSpecialty() {
		LOG.debug("{}", "All internment clinical specialty");
		return ResponseEntity.ok()
                .body(masterDataService.findAllRestrictedBy(
                        ClinicalSpecialty.class, "clinicalSpecialtyTypeId", ClinicalSpecialtyType.Specialty)
                .stream().sorted()
				.collect(Collectors.toList()));
	}
    
    @GetMapping(value = "/discharge/type")
    public ResponseEntity<Collection<MasterDataDto>> getDischargeType(){
        LOG.debug("All internment discharge types ");
        return ResponseEntity.ok().body(dischargeExternalService.internmentGetOf());
    }
    
    
}
