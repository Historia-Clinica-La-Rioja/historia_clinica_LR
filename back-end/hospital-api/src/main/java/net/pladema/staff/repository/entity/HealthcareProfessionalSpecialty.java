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
@Table(name = "healthcare_professional_specialty")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(SGXAuditListener.class)
@AllArgsConstructor
public class HealthcareProfessionalSpecialty extends SGXAuditableEntity<Integer> implements Serializable {
	
	private static final long serialVersionUID = -5292560767942911734L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "professional_profession_id", nullable = false)
	private Integer professionalProfessionId;
	
	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;

	public HealthcareProfessionalSpecialty(Integer professionalProfessionId,
										   Integer clinicalSpecialtyId){
		this.professionalProfessionId = professionalProfessionId;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
	}
}
