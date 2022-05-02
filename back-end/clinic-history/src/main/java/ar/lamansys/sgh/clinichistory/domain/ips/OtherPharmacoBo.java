package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherPharmacoBo {

	private Integer indicationId;

	private SnomedBo snomed;

	private DosageBo dosage;

}
