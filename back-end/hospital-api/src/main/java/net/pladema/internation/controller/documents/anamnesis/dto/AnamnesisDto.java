package net.pladema.internation.controller.documents.anamnesis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.constraints.DiagnosisValid;
import net.pladema.internation.controller.constraints.HealthHistoryConditionValid;
import net.pladema.internation.controller.documents.dto.DocumentDto;
import net.pladema.internation.controller.internment.dto.DocumentObservationsDto;
import net.pladema.internation.controller.ips.dto.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AnamnesisDto implements DocumentDto, Serializable {

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private DocumentObservationsDto notes;

    @NotNull(message = "{diagnosis.mandatory}")
    private HealthConditionDto mainDiagnosis;

    @NotNull
    @DiagnosisValid
    private List<@Valid DiagnosisDto> diagnosis = new ArrayList<>();

    @NotNull
    @HealthHistoryConditionValid
    private List<@Valid HealthHistoryConditionDto> personalHistories = new ArrayList<>();

    @NotNull
    @HealthHistoryConditionValid
    private List<@Valid HealthHistoryConditionDto> familyHistories = new ArrayList<>();

    @NotNull
    private  List<@Valid MedicationDto> medications = new ArrayList<>();

    @NotNull
    private List<@Valid InmunizationDto> inmunizations= new ArrayList<>();

    @NotNull
    private List<@Valid AllergyConditionDto> allergies = new ArrayList<>();

    @Valid
    @Nullable
    private AnthropometricDataDto anthropometricData;

    @Valid
    @Nullable
    private VitalSignDto vitalSigns;

}
