package ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @NotNull
    Integer clinicalSpecialtyId;

    @NotNull
    private String counterReferenceNote;

    private List<@Valid CounterReferenceProcedureDto> procedures = new ArrayList<>();

    private List<@Valid CounterReferenceMedicationDto> medications = new ArrayList<>();

    private List<@Valid CounterReferenceAllergyDto> allergies = new ArrayList<>();

    private List<Integer> fileIds = new ArrayList<>();

	@NotNull
	private Short closureTypeId;
}