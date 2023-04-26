package net.pladema.clinichistory.requests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExtendedServiceRequestBo extends GenericServiceRequestBo {

	private Integer medicalCoverageId;

}
