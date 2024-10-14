package net.pladema.snowstorm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.snowstorm.domain.FullySpecifiedNamesBo;
import net.pladema.snowstorm.domain.PreferredTermBo;
import net.pladema.snowstorm.repository.domain.SnomedSearchItemVo;;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SnomedSearchItemBo {

	private String conceptId;

	private String id;

	private FullySpecifiedNamesBo fsn;

	private PreferredTermBo pt;

	public SnomedSearchItemBo(String conceptId) {
		this.conceptId = conceptId;
	}

	public SnomedSearchItemBo(SnomedSearchItemVo snomedSearchVo) {
		this.conceptId = snomedSearchVo.getSctid();
		this.id = snomedSearchVo.getSnomedId().toString();
		this.pt = new PreferredTermBo(snomedSearchVo.getPt(), "es");
		this.fsn = new FullySpecifiedNamesBo(snomedSearchVo.getPt(), "es");
	}

}
