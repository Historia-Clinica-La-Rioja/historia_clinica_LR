package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyOrderWorkListFilterDto {

	private List<String> categories;
	private List<Short> sourceTypeIds;
	private List<Short> studyTypeIds;
	private Boolean requiresTransfer;
	private Boolean notRequiresTransfer;
	private List<Short> patientTypeId;

}
