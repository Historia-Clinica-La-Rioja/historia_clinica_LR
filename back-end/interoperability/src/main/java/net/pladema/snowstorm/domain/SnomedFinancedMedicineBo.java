package net.pladema.snowstorm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SnomedFinancedMedicineBo extends SnomedSearchItemBo  {

	private boolean financed;

	public SnomedFinancedMedicineBo(String conceptId, String snomedId, FullySpecifiedNamesBo fsn, PreferredTermBo pt, boolean financed) {
		super(conceptId, snomedId, fsn, pt);
		this.financed = financed;
	}
}
