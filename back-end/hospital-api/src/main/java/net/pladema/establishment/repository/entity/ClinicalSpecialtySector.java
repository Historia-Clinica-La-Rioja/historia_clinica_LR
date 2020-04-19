package net.pladema.establishment.repository.entity;

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
@Table(name = "clinical_specialty_sector")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClinicalSpecialtySector implements Serializable{

	private static final long serialVersionUID = 8754509872631360080L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "sector_id", nullable = false)
    private Integer sectorId;
	
	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;
	
	@Column(name = "description", nullable = false)
	private String description;
	
}
