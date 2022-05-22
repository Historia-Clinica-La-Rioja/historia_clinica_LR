package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PharmacoSummaryBo extends IndicationBo {

	private SnomedBo snomed;

	private DosageBo dosage;

	private String via;

}