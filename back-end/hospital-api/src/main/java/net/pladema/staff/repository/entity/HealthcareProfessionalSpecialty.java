package net.pladema.staff.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "healthcare_professional_specialty")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HealthcareProfessionalSpecialty implements Serializable {
	
	private static final long serialVersionUID = -5292560767942911734L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "description", nullable = false, length = 255	)
	private String description;
	
	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;
	
	@Column(name = "professional_specialty_id", nullable = false)
	private Integer professionalSpecialtyId;

	@Column(name = "clinical_specialty_id")
	private Integer clinicalSpecialtyId;
	
}
