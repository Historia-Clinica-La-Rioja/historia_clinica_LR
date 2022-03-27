package net.pladema.clinichistory.hospitalization.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class InternmentGeneralStateDto implements Serializable {

    @NotNull
    private List<DiagnosisDto> diagnosis = new ArrayList<>();

    @NotNull
    private List<HealthHistoryConditionDto> personalHistories = new ArrayList<>();

    @NotNull
    private List<HealthHistoryConditionDto> familyHistories = new ArrayList<>();

    @NotNull
    private List<MedicationDto> medications = new ArrayList<>();

    @NotNull
    private List<ImmunizationDto> immunizations= new ArrayList<>();

    @NotNull
    private List<AllergyConditionDto> allergies = new ArrayList<>();

    @NotNull
    private AnthropometricDataDto anthropometricData;

    @NotNull
    private Last2RiskFactorsDto riskFactors;

}
