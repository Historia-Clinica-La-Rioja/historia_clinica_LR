package net.pladema.staff.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "professional_professions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(SGXAuditListener.class)
public class ProfessionalProfessions extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 7788785116152807155L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;

	@Column(name = "professional_specialty_id")
	private Integer professionalSpecialtyId;

	public ProfessionalProfessions(Integer healthcareProfessionalId, Integer professionalSpecialtyId){
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.professionalSpecialtyId = professionalSpecialtyId;
	}
}
