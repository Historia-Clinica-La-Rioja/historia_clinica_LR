package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
public class OdontologyConsultationDto {

    @NotNull(message = "{value.mandatory}")
    private Integer clinicalSpecialtyId;

    @Nullable
    private List<@Valid OdontologyDentalActionDto> dentalActions = new ArrayList<>();

    @Nullable
    private ReferableItemDto<@Valid OdontologyAllergyConditionDto> allergies;

    @Nullable
    private List<@Valid OdontologyReasonDto> reasons = new ArrayList<>();

    @Nullable
    private List<@Valid OdontologyDiagnosticDto> diagnostics = new ArrayList<>();

    @Nullable
    private List<@Valid OdontologyProcedureDto> procedures = new ArrayList<>();

	@Nullable
	private List<@Valid CreateOutpatientServiceRequestDto> serviceRequests = new ArrayList<>();

    @Nullable
    private ReferableItemDto<@Valid OdontologyPersonalHistoryDto> personalHistories;

    @Nullable
    private List<@Valid OdontologyMedicationDto> medications = new ArrayList<>();

    @Nullable
    private List<@Valid ReferenceDto> references = new ArrayList<>();

    @Nullable
    private String evolutionNote;

    @Nullable
    private Integer permanentTeethPresent;

    @Nullable
    private Integer temporaryTeethPresent;

	@Nullable
	private Integer patientMedicalCoverageId;

	@Nullable
	private Integer hierarchicalUnitId;
}
