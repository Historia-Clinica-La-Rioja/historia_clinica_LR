package ar.lamansys.virtualConsultation.infrastructure.output.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "v_virtual_consultation_professional_amount")
@Entity
public class VVirtualConsultationProfessionalAmount {

	@Id
	@Column(name = "virtual_consultation_id")
	private Integer virtualConsultationId;

	@Column(name = "available_professional_amount")
	private Integer availableProfessionalAmount;

}
