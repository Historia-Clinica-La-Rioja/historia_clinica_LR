package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.pladema.vademecum.domain.SnomedBo;

import javax.annotation.Nullable;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StudyOrderWorkListBo {

	private Integer studyId;

	private SnomedBo snomed;

	private Integer studyTypeId;

	private boolean requiresTransfer;

	private Integer sourceTypeId;

	@Nullable
	private LocalDateTime deferredDate;

	private Integer status;

}
