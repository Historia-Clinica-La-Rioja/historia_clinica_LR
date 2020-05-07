package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.ips.dto.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class InternmentGeneralStateDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    @NotEmpty(message = "{diagnosis.mandatory}")
    private List<DiagnosisDto> diagnosis;

    @NotNull
    private List<HealthHistoryConditionDto> personalHistories = new ArrayList<>();

    @NotNull
    private List<HealthHistoryConditionDto> familyHistories = new ArrayList<>();

    @NotNull
    private List<MedicationDto> medications = new ArrayList<>();

    @NotNull
    private List<InmunizationDto> inmunizations= new ArrayList<>();

    @NotNull
    private List<AllergyConditionDto> allergies = new ArrayList<>();

    @NotNull
    private AnthropometricDataDto anthropometricData;

    @NotNull
    private List<VitalSignDto> vitalSigns = new ArrayList<>();

}
