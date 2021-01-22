package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.domain.PatientSummaryVo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PatientSummaryDto {

    public static final PatientSummaryDto EMPTY = new PatientSummaryDto();

    public PatientSummaryDto(PatientSummaryVo patientSummaryVo){
        this.patient = new PatientInteroperabilityDto(patientSummaryVo.getPatient());

        this.conditions = new ArrayList<>();
        patientSummaryVo.getConditions().forEach(condition ->
            this.conditions.add(new ConditionDto(condition))
        );

        this.medications = new ArrayList<>();
        patientSummaryVo.getMedications().forEach(medication ->
                this.medications.add(new MedicationInteroperabilityDto(medication))
        );

        this.immunizations = new ArrayList<>();
        patientSummaryVo.getImmunizations().forEach(immunization ->
                this.immunizations.add(new ImmunizationInteroperabilityDto(immunization))
        );

        this.allergies = new ArrayList<>();
        patientSummaryVo.getAllergies().forEach(allergy ->
                this.allergies.add(new AllergyIntoleranceDto(allergy))
        );

        this.organization = new OrganizationDto(patientSummaryVo.getOrganization());


    }

    private PatientInteroperabilityDto patient;

    private OrganizationDto organization;

    private List<ConditionDto> conditions;

    private List<MedicationInteroperabilityDto> medications;

    private List<ImmunizationInteroperabilityDto> immunizations;

    private List<AllergyIntoleranceDto> allergies;

    public static PatientSummaryDto emptyPatientSummary(){
        return EMPTY;
    }
}
