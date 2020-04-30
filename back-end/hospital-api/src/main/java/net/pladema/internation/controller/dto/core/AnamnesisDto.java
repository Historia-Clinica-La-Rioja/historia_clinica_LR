package net.pladema.internation.controller.dto.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.DocumentObservationsDto;
import net.pladema.internation.controller.dto.ips.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AnamnesisDto implements Serializable {

    private String documentStatusId;

    private DocumentObservationsDto notes;

    @NotNull
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

    private AnthropometricDataDto anthropometricData;

    private VitalSignDto vitalSigns;

}
