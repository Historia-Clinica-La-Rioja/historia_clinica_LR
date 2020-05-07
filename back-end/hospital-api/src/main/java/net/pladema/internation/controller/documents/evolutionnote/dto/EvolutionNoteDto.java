package net.pladema.internation.controller.documents.evolutionnote.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.internment.dto.DocumentObservationsDto;
import net.pladema.internation.controller.ips.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionNoteDto implements Serializable {

    @NotNull
    private boolean confirmed = false;

    private DocumentObservationsDto notes;

    @NotNull
    private List<@Valid DiagnosisDto> diagnosis = new ArrayList<>();

    @NotNull
    private List<@Valid InmunizationDto> inmunizations = new ArrayList<>();

    @NotNull
    private List<@Valid AllergyConditionDto> allergies = new ArrayList<>();

    @Valid
    private AnthropometricDataDto anthropometricData;

    @Valid
    private VitalSignDto vitalSigns;

}
