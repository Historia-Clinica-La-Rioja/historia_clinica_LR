package net.pladema.snowstorm.services.domain.semantics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "snomed-semantics")
@PropertySource(value = "classpath:snomed-semantics.properties")
@ToString
public class SnomedSemantics {

    @ToString.Include
    private EclOperators operators;

    @Value("${snomed-semantics.bodyStructure.ecl}")
    @ToString.Include
    private String bodyStructureEcl;

    @Value("${snomed-semantics.clinicalFinding.ecl}")
    @ToString.Include
    private String clinicalFindingEcl;

    @Value("${snomed-semantics.event.ecl}")
    @ToString.Include
    private String eventEcl;

    @Value("${snomed-semantics.situation.ecl}")
    @ToString.Include
    private String situtationEcl;

    @Value("${snomed-semantics.familyHistoricContext.ecl}")
    @ToString.Include
    private String familyHistoricContextEcl;

    @Value("${snomed-semantics.socialContext.ecl}")
    @ToString.Include
    private String socialContextEcl;

    @Value("${snomed-semantics.drug.ecl}")
    @ToString.Include
    private String drugEcl;

    @Value("${snomed-semantics.substance.ecl}")
    @ToString.Include
    private String substanceEcl;

    @Value("${snomed-semantics.aboFinding.ecl}")
    @ToString.Include
    private String aboFindingEcl;

    @Value("${snomed-semantics.disorder.ecl}")
    @ToString.Include
    private String disorderEcl;

    @Value("${snomed-semantics.medicinalProduct.ecl}")
    @ToString.Include
    private String medicinalProductEcl;

    @Value("${snomed-semantics.procedure.ecl}")
    @ToString.Include
    private String procedureEcl;

    @Value("${snomed-semantics.allergicDisposition.ecl}")
    @ToString.Include
    private String allergicDispositionEcl;

    @Value("${snomed-semantics.reportableImmunizationsRefset.ecl}")
    @ToString.Include
    private String reportableImmunizationsRefsetEcl;

    @Value("${snomed-semantics.genericMedicine.ecl}")
    @ToString.Include
    private String genericMedicineEcl;

    @Value("${snomed-semantics.diagnosis.ecl}")
    @ToString.Include
    private String diagnosisEcl;

    @Value("${snomed-semantics.bloodType.ecl}")
    @ToString.Include
    private String bloodTypeEcl;

    @Value("${snomed-semantics.personalRecord.ecl}")
    @ToString.Include
    private String personalRecordEcl;

    @Value("${snomed-semantics.familyRecord.ecl}")
    @ToString.Include
    private String familyRecordEcl;

    @Value("${snomed-semantics.allergy.ecl}")
    @ToString.Include
    private String allergyEcl;

    @Value("${snomed-semantics.hospitalizationReason.ecl}")
    @ToString.Include
    private String hospitalizationReasonEcl;

    @Value("${snomed-semantics.vaccine.ecl}")
    @ToString.Include
    private String vaccineEcl;

    @Value("${snomed-semantics.medicine.ecl}")
    @ToString.Include
    private String medicineEcl;

    @Value("${snomed-semantics.procedureGroup.ecl}")
    @ToString.Include
    private String procedureGroupEcl;

    @Value("${snomed-semantics.consultationReason.ecl}")
    @ToString.Include
    private String consultationReasonEcl;

    @Value("${snomed-semantics.illegallyInducedAbortion.ecl}")
    @ToString.Include
    private String illegallyInducedAbortionEcl;

    @Value("${snomed-semantics.illegallyInducedAbortion.illegalAbortion.ecl}")
    @ToString.Include
    private String illegalAbortion;

    @Value("${snomed-semantics.illegallyInducedAbortion.hospitalReadmission.ecl}")
    @ToString.Include
    private String hospitalReadmissionEcl;

    @Value("${snomed-semantics.illegallyInducedAbortion.illegalAbortionInfection.ecl}")
    @ToString.Include
    private String illegalAbortionInfectionEcl;

    @Value("${snomed-semantics.illegallyInducedAbortion.illegalAbortionExcessiveBleeding.ecl}")
    @ToString.Include
    private String illegalAbortionExcessiveBleedingEcl;

    @Value("${snomed-semantics.illegallyInducedAbortion.illegalAbortionSecondaryBleeding.ecl}")
    @ToString.Include
    private String illegalAbortionSecondaryBleedingEcl;

    private Map<SnomedECL, String> snomedECLStringMap;

    @PostConstruct
    public void loadMap() {
        this.snomedECLStringMap = new HashMap<>();
        snomedECLStringMap.put(SnomedECL.DIAGNOSIS, diagnosisEcl);
        snomedECLStringMap.put(SnomedECL.BLOOD_TYPE, bloodTypeEcl);
        snomedECLStringMap.put(SnomedECL.PERSONAL_RECORD, personalRecordEcl);
        snomedECLStringMap.put(SnomedECL.FAMILY_RECORD, familyRecordEcl);
        snomedECLStringMap.put(SnomedECL.ALLERGY, allergyEcl);
        snomedECLStringMap.put(SnomedECL.HOSPITAL_REASON, hospitalizationReasonEcl);
        snomedECLStringMap.put(SnomedECL.VACCINE, vaccineEcl);
        snomedECLStringMap.put(SnomedECL.MEDICINE, medicineEcl);
        snomedECLStringMap.put(SnomedECL.PROCEDURE, procedureGroupEcl);
        snomedECLStringMap.put(SnomedECL.CONSULTATION_REASON, consultationReasonEcl);
    }

    public String getEcl(SnomedECL key) {
        return snomedECLStringMap.get(key);
    }
}