package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EpicrisisDto implements Serializable {

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private EpicrisisObservationsDto notes;

    @NotNull
    private DiagnosisDto mainDiagnosis;

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

}
