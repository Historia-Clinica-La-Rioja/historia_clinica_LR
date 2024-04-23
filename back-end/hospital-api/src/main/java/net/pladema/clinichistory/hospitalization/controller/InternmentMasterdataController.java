package net.pladema.clinichistory.hospitalization.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticTechnique;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EBreathing;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ECircuit;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ETrachealIntubation;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnit;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EVia;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherIndicationType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceCategory;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceCriticality;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.BloodType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.HealthConditionSeverity;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.emergencycare.controller.DischargeTypeMasterDataExternalService;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterDataProjection;
import ar.lamansys.sgx.shared.masterdata.application.MasterDataService;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.ClinicalSpecialtyType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;


@RequestMapping("/internments/masterdata")
@Tag(name = "Internments master data", description = "Internments master data")
@Slf4j
@RequiredArgsConstructor
@RestController
public class InternmentMasterdataController {

    private final MasterDataService masterDataService;

    private final DischargeTypeMasterDataExternalService dischargeExternalService;
    

    @GetMapping(value = "/allergy/category")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyCategory(){
        log.debug("{}", "All allergy intolerance category");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceCategory.class));
    }

    @GetMapping(value = "/allergy/clinical")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyClinical(){
        log.debug("{}", "All allergy intolerance clinical status");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceClinicalStatus.class));
    }

    @GetMapping(value = "/allergy/verification")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyVerification(){
        log.debug("{}", "All allergy intolerance verification status");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceVerificationStatus.class));
    }

    @GetMapping(value = "/allergy/criticality")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyCriticality(){
        log.debug("{}", "All allergy intolerance criticality");
        return ResponseEntity.ok().body(masterDataService.findAll(AllergyIntoleranceCriticality.class));
    }

    @GetMapping(value = "/health/clinical")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthClinical(){
        log.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(masterDataService.findAll(ConditionClinicalStatus.class));
    }

    @GetMapping(value = "/health/clinical/down")
    public ResponseEntity<Collection<MasterDataProjection>> getDownHealthClinical(){
        log.debug("{}", "All health condition clinical status");
        return ResponseEntity.ok().body(masterDataService.findAll(ConditionClinicalStatus.class,
                ConditionClinicalStatus.downState()));
    }

    @GetMapping(value = "/health/verification")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthVerification(){
        log.debug("{}", "All health condition verification status");
        return ResponseEntity.ok().body(masterDataService.findAll(ConditionVerificationStatus.class));
    }

    @GetMapping(value = "/health/severity")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthSeverity(){
        log.debug("{}", "All health condition severity");
        return ResponseEntity.ok().body(masterDataService.findAll(HealthConditionSeverity.class));
    }

    @GetMapping(value = "/health/verification/down")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthVerificationDown(){
        log.debug("{}", "All health condition verification status down");
        return ResponseEntity.ok().body(masterDataService.findAll(
                ConditionVerificationStatus.class,
                ConditionVerificationStatus.downState()));
    }

    @GetMapping(value = "/medication")
    public ResponseEntity<Collection<MasterDataProjection>> getHealthMedication(){
        log.debug("{}", "All medication statement status");
        return ResponseEntity.ok().body(masterDataService.findAll(MedicationStatementStatus.class));
    }

    @GetMapping(value = "/inmunization")
    public ResponseEntity<Collection<MasterDataProjection>> getInmunization(){
        log.debug("{}", "All inmunization status");
        return ResponseEntity.ok().body(masterDataService.findAll(InmunizationStatus.class));
    }

    @GetMapping(value = "/document/status")
    public ResponseEntity<Collection<MasterDataProjection>> getDocumentStatus(){
        log.debug("{}", "All document status");
        return ResponseEntity.ok().body(masterDataService.findAll(DocumentStatus.class));
    }

    @GetMapping(value = "/document/type")
    public ResponseEntity<Collection<MasterDataDto>> getDocumentType(){
        log.debug("{}", "All internment document types");
		Collection<MasterDataDto> internmentDocumentTypes = EDocumentType.getAllInternmentDocumentTypes()
				.stream()
				.map(eDocumentType -> {
					MasterDataDto result = new MasterDataDto();
					result.setId(eDocumentType.getId());
					result.setDescription(eDocumentType.getDescription());
					return result;
				})
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(internmentDocumentTypes);
    }

    @GetMapping(value = "/observation")
    public ResponseEntity<Collection<MasterDataProjection>> getObservation(){
        log.debug("{}", "All observation");
        return ResponseEntity.ok().body(masterDataService.findAll(ObservationStatus.class));
    }

    @GetMapping(value = "/episode")
    public ResponseEntity<Collection<MasterDataProjection>> getInternmentEpisode(){
        log.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(masterDataService.findAll(InternmentEpisodeStatus.class));
    }

    @GetMapping(value = "/bloodtypes")
    public ResponseEntity<Collection<MasterDataProjection>> getBloodTypes(){
        log.debug("{}", "All internment episode status");
        return ResponseEntity.ok().body(masterDataService.findAll(BloodType.class));
    }
    
	@GetMapping(value = "/clinical/specialty")
	public ResponseEntity<Collection<MasterDataProjection>> getClinicalSpecialty() {
		log.debug("{}", "All internment clinical specialty");
		return ResponseEntity.ok()
                .body(masterDataService.findAllRestrictedBy(
                        ClinicalSpecialty.class, "clinicalSpecialtyTypeId", ClinicalSpecialtyType.Specialty)
                .stream().sorted()
				.collect(Collectors.toList()));
	}
    
    @GetMapping(value = "/discharge/type")
    public ResponseEntity<Collection<MasterDataDto>> getDischargeType(){
        log.debug("All internment discharge types ");
        return ResponseEntity.ok().body(dischargeExternalService.internmentGetOf());
    }


	@GetMapping(value = "/other-indication-type")
	public ResponseEntity<Collection<MasterDataProjection>> getOtherIndicationTypes(){
		log.debug("{}", "All internment episode status");
		return ResponseEntity.ok().body(masterDataService.findAll(OtherIndicationType.class));
	}

	@GetMapping(value = "/vias")
	public ResponseEntity<Collection<MasterDataDto>> getVias() {
		log.debug("All via parenteral-plan types");
        var result = EnumWriter.writeList(EVia.getAllParenteral());
        log.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/vias-pharmaco")
	public ResponseEntity<Collection<MasterDataDto>> getViasPharmaco() {
		log.debug("All via pharmaco types");
        var result = EnumWriter.writeList(EVia.getAllPharmaco());
        log.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

    @GetMapping(value = "/vias-premedication")
    public ResponseEntity<Collection<MasterDataDto>> getViasPreMedication() {
        log.debug("All via premedication types");
        var result = EnumWriter.writeList(EVia.getPreMedication());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/vias-anesthetic-plan")
    public ResponseEntity<Collection<MasterDataDto>> getViasAnestheticPlan() {
        log.debug("All via anesthetic-plan types");
        var result = EnumWriter.writeList(EVia.getAnestheticPlan());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/vias-anesthetic-agent")
    public ResponseEntity<Collection<MasterDataDto>> getViasAnestheticAgent() {
        log.debug("All via anesthetic-agent types");
        var result = EnumWriter.writeList(EVia.getAnestheticAgent());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/vias-non-anesthetic-drug")
    public ResponseEntity<Collection<MasterDataDto>> getViasNonAnestheticDrug() {
        log.debug("All via non-anesthetic-drug types");
        var result = EnumWriter.writeList(EVia.getNonAnestheticDrug());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/vias-antibiotic-prophylaxis")
    public ResponseEntity<Collection<MasterDataDto>> getViasAntibioticProphylaxis() {
        log.debug("All via antibiotic-prophylaxis types");
        var result = EnumWriter.writeList(EVia.getAntibioticProphylaxis());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping(value = "/units")
	public ResponseEntity<Collection<MasterDataDto>> getUnits() {
		log.debug("{}", "All units types");
		return ResponseEntity.ok().body(EnumWriter.writeList(EUnit.getAll()));
	}

    @GetMapping(value = "/anesthetic-technique")
    public ResponseEntity<Collection<MasterDataDto>> getAnestheticTechniqueTypes() {
        log.debug("All anesthetic technique types");
        var result = EnumWriter.writeList(EAnestheticTechnique.getAll());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/tracheal-intubation")
    public ResponseEntity<Collection<MasterDataDto>> getTrachealIntubationTypes() {
        log.debug("All tracheal intubation types");
        var result = EnumWriter.writeList(ETrachealIntubation.getAll());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/breathing")
    public ResponseEntity<Collection<MasterDataDto>> getBreathingTypes() {
        log.debug("All breathing types");
        var result = EnumWriter.writeList(EBreathing.getAll());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/circuit")
    public ResponseEntity<Collection<MasterDataDto>> getCircuitTypes() {
        log.debug("{}", "All circuit types");
        var result = EnumWriter.writeList(ECircuit.getAll());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
