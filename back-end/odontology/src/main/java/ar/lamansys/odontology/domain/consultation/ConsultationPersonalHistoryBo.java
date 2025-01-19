package ar.lamansys.odontology.domain.consultation;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultationPersonalHistoryBo extends ClinicalTermBo {

	private LocalDate startDate;
	private LocalDate inactivationDate;
	private Short typeId;
	private String note;

	public ConsultationPersonalHistoryBo(LocalDate startDate) {
		this.startDate = startDate;
	}
}
