package net.pladema.internation.controller.dto.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.constraints.AnamnesisDiagnosisValid;
import net.pladema.internation.controller.dto.DocumentObservationsDto;
import net.pladema.internation.controller.dto.ips.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AnamnesisDto implements Serializable {

    @NotNull
    private boolean confirmed = false;

    private DocumentObservationsDto notes;

    @NotNull
    private List<@Valid DiagnosisDto> diagnosis;

    @NotNull
    private List<@Valid HealthHistoryConditionDto> personalHistories = new ArrayList<>();

    @NotNull
    private List<@Valid HealthHistoryConditionDto> familyHistories = new ArrayList<>();

    @NotNull
    private  List<@Valid MedicationDto> medications = new ArrayList<>();

    @NotNull
    private List<@Valid InmunizationDto> inmunizations= new ArrayList<>();

    @NotNull
    private List<@Valid AllergyConditionDto> allergies = new ArrayList<>();

    @Valid
    private AnthropometricDataDto anthropometricData;

    @Valid
    private VitalSignDto vitalSigns;

}
