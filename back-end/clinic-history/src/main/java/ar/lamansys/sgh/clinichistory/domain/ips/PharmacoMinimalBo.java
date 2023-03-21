package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PharmacoMinimalBo {

	private Integer snomedId;
	private Double value;
	private String unit;
	private Short via;
	private Long total;

	public PharmacoMinimalBo(Integer snomedId, Double value, String unit, Short via, Long total) {
		this.snomedId = snomedId;
		this.value = value;
		this.unit = unit;
		this.via = via;
		this.total = total;
	}

}
