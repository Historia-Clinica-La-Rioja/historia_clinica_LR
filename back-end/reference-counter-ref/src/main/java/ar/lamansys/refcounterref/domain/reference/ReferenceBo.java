package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class ReferenceBo {

    private String note;

    private Boolean consultation;

    private Boolean procedure;

    @NotNull(message = "{value.mandatory}")
    private Integer encounterId;

    @NotNull(message = "{value.mandatory}")
    private Integer sourceTypeId;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private List<ReferenceProblemBo> problems;

    private Integer careLineId;

    @NotNull(message = "{value.mandatory}")
    private List<Integer> clinicalSpecialtyIds;

    private List<Integer> fileIds;

    private Integer destinationInstitutionId;

	private String phoneNumber;

	private String phonePrefix;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private Integer priority;

	private ReferenceStudyBo study;

}
