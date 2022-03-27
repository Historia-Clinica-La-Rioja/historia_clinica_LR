package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateOutpatientDto {

    @Nullable
    private String evolutionNote;

    private List<@Valid OutpatientReasonDto> reasons = new ArrayList<>();

    private List<@Valid OutpatientProblemDto> problems = new ArrayList<>();

    private List<@Valid OutpatientProcedureDto> procedures = new ArrayList<>();

    private List<@Valid OutpatientFamilyHistoryDto> familyHistories = new ArrayList<>();

    private  List<@Valid OutpatientMedicationDto> medications = new ArrayList<>();

    private List<@Valid OutpatientAllergyConditionDto> allergies = new ArrayList<>();

    private List<@Valid ReferenceDto> references = new ArrayList<>();

    @Valid
    @Nullable
    private OutpatientAnthropometricDataDto anthropometricData;

    @Valid
    @Nullable
    private OutpatientRiskFactorDto riskFactors;

    @Valid
    @Nullable
    private Integer clinicalSpecialtyId;



}
