package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReferenceRequestClosureBo {

	private Integer referenceId;
	private Integer clinicalSpecialtyId;
	private String counterReferenceNote;
	private Short closureTypeId;
	private List<Integer> fileIds;

}
