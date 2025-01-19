package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceDto implements Serializable {

    private static final long serialVersionUID = -3184712067120494370L;

    @Nullable
    private String note;

    @Nullable
    private Boolean consultation;

    @Nullable
    private Boolean procedure;

	@Nullable
    private Integer careLineId;

	@Nullable
    private List<Integer> clinicalSpecialtyIds;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private List<ReferenceProblemDto> problems;

    private List<Integer> fileIds;

	@Valid
    private Integer destinationInstitutionId;

	@Nullable
	private String phoneNumber;

	@Nullable
	private String phonePrefix;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private Integer priority;

	@Nullable
	private ReferenceStudyDto study;

}