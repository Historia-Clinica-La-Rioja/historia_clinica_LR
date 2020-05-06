package net.pladema.internation.controller.documents.epicrisis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.ips.dto.*;

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

    private EpicrisisObservationsDto notes;

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
