package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private List<@Valid ImmunizationDto> immunizations= new ArrayList<>();

    @NotNull
    private List<@Valid AllergyConditionDto> allergies = new ArrayList<>();

	@Nullable
	private String modificationReason;

}
