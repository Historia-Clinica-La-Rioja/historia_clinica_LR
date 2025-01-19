package ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference;

import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto;
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
public class CounterReferenceDto implements Serializable {

    private static final long serialVersionUID = 476188435911577990L;

    @NotNull
    private Integer referenceId;


    private Integer clinicalSpecialtyId;

    @NotNull
    private String counterReferenceNote;

    private List<@Valid CounterReferenceProcedureDto> procedures = new ArrayList<>();

    private List<@Valid CounterReferenceMedicationDto> medications = new ArrayList<>();

    private ReferableItemDto<@Valid CounterReferenceAllergyDto> allergies;

    private List<Integer> fileIds = new ArrayList<>();

	@NotNull
	private Short closureTypeId;

	@Nullable
	private Integer patientMedicalCoverageId;

	@Nullable
	private Integer hierarchicalUnitId;
}