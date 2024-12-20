package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudyOrderWorkListFilterBo {

	private List<String> categories;
	private List<Short> sourceTypeIds;
	private List<Short> studyTypeIds;
	private Boolean requiresTransfer;
	private Boolean notRequiresTransfer;
	private List<Short> patientTypeId;

}
