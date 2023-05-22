package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedBo {

	private String pt;
	private String sctid;
}
