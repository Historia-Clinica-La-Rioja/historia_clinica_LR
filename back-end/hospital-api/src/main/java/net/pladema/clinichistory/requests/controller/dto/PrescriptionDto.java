package net.pladema.clinichistory.requests.controller.dto;


import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.requests.servicerequests.domain.enums.EStudyType;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionDto implements Serializable {

    private boolean hasRecipe = false;

    @Nullable
    private Integer medicalCoverageId;

	@Nullable
	private Boolean isPostDated;

	@Nullable
	private Integer repetitions;

	@Nullable
	private Integer clinicalSpecialtyId;

	@Nullable
	private Boolean isArchived;

    @NotEmpty
    private List<@Valid PrescriptionItemDto> items = new ArrayList<>();

	@Nullable
	private String observations;

	@Nullable
	private EStudyType studyType;

	@Nullable
	private Boolean requiresTransfer;

	@Nullable
	private DateTimeDto deferredDate;
}
