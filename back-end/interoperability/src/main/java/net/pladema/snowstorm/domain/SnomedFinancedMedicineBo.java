package net.pladema.snowstorm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SnomedFinancedMedicineBo extends SnomedSearchItemBo  {

	private boolean financed;
	private List<String> auditRequiredText;

	public SnomedFinancedMedicineBo(String conceptId, String snomedId, FullySpecifiedNamesBo fsn, PreferredTermBo pt, boolean financed, List<String> auditRequiredText) {
		super(conceptId, snomedId, fsn, pt);
		this.financed = financed;
		this.auditRequiredText = auditRequiredText;
	}
}
