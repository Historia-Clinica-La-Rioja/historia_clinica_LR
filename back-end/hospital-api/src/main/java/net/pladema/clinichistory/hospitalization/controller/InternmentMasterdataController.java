package net.pladema.clinichistory.hospitalization.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.EUnit;
import ar.lamansys.sgh.clinichistory.domain.ips.EVia;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherIndicationType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.*;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.emergencycare.controller.DischargeTypeMasterDataExternalService;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterDataProjection;
import ar.lamansys.sgx.shared.masterdata.application.MasterDataService;
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
@Tag(name = "Internments master data", description = "Internments master data")
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

    @GetMapping(value = "/allergy/criticality")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyCriticality(){
        LOG.debug("{}", "All allergy intolerance criticality");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceCriticality.class));
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

    @GetMapping(value = "/health/severity")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthSeverity(){
        LOG.debug("{}", "All health condition severity");
        return ResponseEntity.ok().body(masterDataService.findAll(HealthConditionSeverity.class));
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


	@GetMapping(value = "/other-indication-type")
	public ResponseEntity<Collection<MasterDataProjection>> getOtherIndicationTypes(){
		LOG.debug("{}", "All internment episode status");
		return ResponseEntity.ok().body(masterDataService.findAll(OtherIndicationType.class));
	}

	@GetMapping(value = "/vias")
	public ResponseEntity<Collection<MasterDataDto>> getVias() {
		LOG.debug("{}", "All via types");
		return ResponseEntity.ok().body(EnumWriter.writeList(EVia.getAll()));
	}

	@GetMapping(value = "/units")
	public ResponseEntity<Collection<MasterDataDto>> getUnits() {
		LOG.debug("{}", "All units types");
		return ResponseEntity.ok().body(EnumWriter.writeList(EUnit.getAll()));
	}
    
}
