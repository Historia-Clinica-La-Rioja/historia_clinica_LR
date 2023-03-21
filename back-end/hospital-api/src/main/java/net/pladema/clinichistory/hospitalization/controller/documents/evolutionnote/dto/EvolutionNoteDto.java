package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
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
public class EvolutionNoteDto implements Serializable {

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private DocumentObservationsDto notes;

    @Nullable
    private HealthConditionDto mainDiagnosis;

    @Nullable
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
    private RiskFactorDto riskFactors;

	@Nullable
	private Boolean isNursingEvolutionNote = false;

	@Nullable
	private String modificationReason;

}
