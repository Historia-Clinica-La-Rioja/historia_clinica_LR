package net.pladema.clinichistory.requests.servicerequests.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.vademecum.domain.SnomedBo;

import javax.annotation.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class StudyOrderWorkListVo {

	private Integer studyId;

	private SnomedBo snomed;

	private Integer studyTypeId;

	private boolean requiresTransfer;

	private Integer sourceTypeId;

	@Nullable
	private LocalDateTime deferredDate;

	private Integer status;

}
