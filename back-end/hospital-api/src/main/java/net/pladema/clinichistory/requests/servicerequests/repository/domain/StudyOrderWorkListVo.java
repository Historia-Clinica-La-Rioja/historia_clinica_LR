package net.pladema.clinichistory.requests.servicerequests.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderBasicPatientBo;
import net.pladema.vademecum.domain.SnomedBo;

import javax.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class StudyOrderWorkListVo {

	public static final String STATUS_PENDING = "1";

	private Integer studyId;

	private StudyOrderBasicPatientBo patientVo;

	private SnomedBo snomed;

	private Short studyTypeId;

	private Boolean requiresTransfer;

	private Short sourceTypeId;

	@Nullable
	private LocalDateTime deferredDate;

	private String status;

	private LocalDateTime createdDate;

	public StudyOrderWorkListVo(Integer studyId, Integer patientId, String firstName, String middleNames, String lastName, String otherLastNames, String nameSelfDetermination,
								String identificationNumber, Short identificationTypeId, Short genderId, String genderDescription, LocalDate birthDate,
								String sctid, String pt, Short studyTypeId, Boolean requiresTransfer,
								Short sourceTypeId, @Nullable LocalDateTime deferredDate, LocalDateTime createdDate) {
		this.studyId = studyId;
		this.patientVo = new StudyOrderBasicPatientBo(patientId, firstName, middleNames, lastName, otherLastNames, nameSelfDetermination,
				identificationNumber, identificationTypeId, genderId, genderDescription, birthDate);
		this.snomed = new SnomedBo(sctid, pt);
		this.studyTypeId = studyTypeId;
		this.requiresTransfer = requiresTransfer;
		this.sourceTypeId = sourceTypeId;
		this.deferredDate = deferredDate;
		this.status = STATUS_PENDING;
		this.createdDate = createdDate;
	}

}
