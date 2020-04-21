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
@Table(name = "professional_specialty")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfessionalSpecialty implements Serializable {

	private static final long serialVersionUID = 2806005942492385531L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "description", nullable = false, length = 255)
	private String description;

	@Column(name = "description_profession_ref", nullable = false, length = 255)
	private String descriptionProfessionRef;

	@Column(name = "sctid_code", nullable = false, length = 20)
	private String sctidCode;
	
	@Column(name = "education_type_id", nullable = false)
	private Short educationTypeId;

}