package ar.lamansys.sgh.publicapi.domain.prescription;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class PrescriptionsDataBo {
	private String domain;
	private String prescriptionId;
	private LocalDateTime prescriptionDate;
	private LocalDateTime dueDate;

	private String link;

	private ProfessionalPrescriptionBo professionalPrescriptionBo;

	public PrescriptionsDataBo(String domain,
                               String prescriptionId,
                               LocalDateTime prescriptionDate,
                               LocalDateTime dueDate,
                               String link,
                               ProfessionalPrescriptionBo professionalPrescriptionBo) {
		this.domain = domain;
		this.prescriptionDate = prescriptionDate;
		this.prescriptionId = prescriptionId;
		this.dueDate = dueDate;
		this.link = link;
		this.professionalPrescriptionBo = professionalPrescriptionBo;
	}
}
