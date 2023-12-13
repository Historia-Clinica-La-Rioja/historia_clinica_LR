package net.pladema.medicalconsultation.virtualConsultation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VirtualConsultationAvailableProfessionalAmountBo {

	private Integer virtualConsultationId;

	private Integer professionalAmount;

}
