package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.*;
import net.pladema.featureflags.controller.constraints.SGHNotNull;
import ar.lamansys.sgx.shared.featureflags.AppFeature;

import javax.annotation.Nullable;
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

    @Nullable
    private DocumentObservationsDto notes;

    @SGHNotNull(message = "{diagnosis.mandatory}", ffs = {AppFeature.MAIN_DIAGNOSIS_REQUIRED})
    private HealthConditionDto mainDiagnosis;

    @NotNull
    private List<@Valid DiagnosisDto> diagnosis = new ArrayList<>();

    @NotNull
    private List<@Valid HealthHistoryConditionDto> personalHistories = new ArrayList<>();

    @Nullable
    private List<@Valid HospitalizationProcedureDto> procedures = new ArrayList<>();

    @NotNull
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
    private RiskFactorDto riskFactors;

	@Nullable
	private String modificationReason;

}
