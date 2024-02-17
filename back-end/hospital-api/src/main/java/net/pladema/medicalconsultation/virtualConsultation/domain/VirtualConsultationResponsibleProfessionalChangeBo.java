package net.pladema.medicalconsultation.virtualConsultation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VirtualConsultationResponsibleProfessionalChangeBo {

	private Integer healthcareProfessionalId;

	private Integer virtualConsultationId;
}
