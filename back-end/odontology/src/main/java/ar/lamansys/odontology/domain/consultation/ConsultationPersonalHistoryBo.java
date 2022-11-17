package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultationPersonalHistoryBo extends ClinicalTermBo {

	private DateDto startDate;

	public ConsultationPersonalHistoryBo(DateDto startDate) {
		this.startDate = startDate;
	}
}
