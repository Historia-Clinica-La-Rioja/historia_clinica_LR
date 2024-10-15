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

	private StudyOrderBasicPatientBo patientBo;

	private SnomedBo snomed;

	private Short studyTypeId;

	private Boolean requiresTransfer;

	private Short sourceTypeId;

	@Nullable
	private LocalDateTime deferredDate;

	private String status;

	private LocalDateTime createdDate;

	private StudyOrderPatientLocationBo patientLocation;

}
