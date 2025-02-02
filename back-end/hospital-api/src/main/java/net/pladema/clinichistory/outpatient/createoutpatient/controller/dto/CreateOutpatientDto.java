package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.CompleteParameterizedFormDto;

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

    private List<@Valid CreateOutpatientProcedureDto> procedures = new ArrayList<>();
	@Nullable
	private List<@Valid CreateOutpatientServiceRequestDto> serviceRequests = new ArrayList<>();

    @Nullable
    private ReferableItemDto<@Valid OutpatientPersonalHistoryDto> personalHistories;

    private ReferableItemDto<@Valid OutpatientFamilyHistoryDto> familyHistories;

    private  List<@Valid OutpatientMedicationDto> medications = new ArrayList<>();

    private ReferableItemDto<@Valid OutpatientAllergyConditionDto> allergies;

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

	@Valid
	@Nullable
	private Integer patientMedicalCoverageId;

	@Valid
	@Nullable
	private Integer hierarchicalUnitId;

	private List<Integer> involvedHealthcareProfessionalIds;

	private List<CompleteParameterizedFormDto> completeForms;

}
