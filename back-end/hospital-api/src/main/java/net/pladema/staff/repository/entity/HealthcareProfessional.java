package net.pladema.staff.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "healthcare_professional")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HealthcareProfessional implements Serializable {

	private static final long serialVersionUID = 7788785116152807155L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "license_number", nullable = false, length = 512)
	private String licenseNumber;

	@Column(name = "person_id", nullable = false, unique = true)
	private Integer personId;
	
	@Column(name = "is_medical_doctor", nullable = false)
	private Boolean isMedicalDoctor;	

}
