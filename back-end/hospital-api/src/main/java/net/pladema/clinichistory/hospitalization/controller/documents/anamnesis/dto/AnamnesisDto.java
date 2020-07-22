package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.*;
import net.pladema.featureflags.controller.constraints.SGHNotNull;
import net.pladema.clinichistory.hospitalization.controller.constraints.DiagnosisValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.HealthHistoryConditionValid;
import net.pladema.clinichistory.hospitalization.controller.documents.DocumentDto;
import net.pladema.clinichistory.hospitalization.controller.dto.DocumentObservationsDto;
import net.pladema.sgx.featureflags.AppFeature;

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

    @SGHNotNull(message = "{diagnosis.mandatory}", ffs = {AppFeature.MAIN_DIAGNOSIS_REQUIRED})
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
    private List<@Valid ImmunizationDto> immunizations= new ArrayList<>();

    @NotNull
    private List<@Valid AllergyConditionDto> allergies = new ArrayList<>();

    @Valid
    @Nullable
    private AnthropometricDataDto anthropometricData;

    @Valid
    @Nullable
    private VitalSignDto vitalSigns;

}
