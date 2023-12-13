package net.pladema.cipres.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SnomedBo {

	private Integer id;

	private String sctid;

	private String pt;

	public SnomedBo(String sctid, String pt) {
		this.sctid = sctid;
		this.pt = pt;
	}

}
