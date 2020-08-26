package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.constraints.DiagnosisValid;
import net.pladema.clinichistory.hospitalization.controller.documents.DocumentDto;
import net.pladema.clinichistory.hospitalization.controller.dto.DocumentObservationsDto;
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
public class EvolutionNoteDto implements DocumentDto, Serializable {

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private DocumentObservationsDto notes;

    @Nullable
    private HealthConditionDto mainDiagnosis;

    @Nullable
    @DiagnosisValid
    private List<@Valid DiagnosisDto> diagnosis = new ArrayList<>();

    @Nullable
    private List<@Valid ImmunizationDto> immunizations = new ArrayList<>();

    @Nullable
    private List<@Valid AllergyConditionDto> allergies = new ArrayList<>();

    @Nullable
    private List<@Valid HospitalizationProcedureDto> procedures = new ArrayList<>();

    @Valid
    @Nullable
    private AnthropometricDataDto anthropometricData;

    @Valid
    @Nullable
    private VitalSignDto vitalSigns;

}
