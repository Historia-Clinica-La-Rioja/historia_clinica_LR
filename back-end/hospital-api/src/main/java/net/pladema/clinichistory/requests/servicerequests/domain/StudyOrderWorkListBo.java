package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.pladema.vademecum.domain.SnomedBo;

import javax.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StudyOrderWorkListBo {

	private Integer studyId;

	private StudyOrderBasicPatientBo patientBo;

	private List<SnomedBo> snomed;

	private Short studyTypeId;

	private Boolean requiresTransfer;

	private Short sourceTypeId;

	@Nullable
	private LocalDateTime deferredDate;

	private String status;

	private LocalDateTime createdDate;

	private StudyOrderPatientLocationBo patientLocation;

	private String emergencyCareReason;

}
