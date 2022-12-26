package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class PharmacoSummaryBo extends IndicationBo {

	private SnomedBo snomed;

	private DosageBo dosage;

	private String via;

	@Nullable
	private String note;

}