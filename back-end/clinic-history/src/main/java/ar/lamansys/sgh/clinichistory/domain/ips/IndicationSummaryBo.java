package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndicationSummaryBo {

	private Integer id;

	private DosageBo dosage;

	private String observation;

	private LocalDate indicationDate;

}
