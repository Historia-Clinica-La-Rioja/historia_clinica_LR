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
@Table(name = "clinical_specialty")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClinicalSpecialty implements Serializable, Comparable<ClinicalSpecialty> {

	private static final long serialVersionUID = -5082786259164464584L;
	public static final String FIX_NAME = "ERRÓNEA-";
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "sctid_code", nullable = false, length = 20)
	private String sctidCode;

	@Column(name = "clinical_specialty_type_id", nullable = false)
	private Short clinicalSpecialtyTypeId;

	@Override
	public int compareTo(ClinicalSpecialty anotherClinicalSpecialty) {
		return name.compareTo(anotherClinicalSpecialty.name);
	}

	public boolean withoutName(){
		return (name == null || name.isBlank() ||name.equals("-1"));
	}

	public boolean isSpecialty(){
		return getClinicalSpecialtyTypeId().equals(ClinicalSpecialtyType.Specialty);
	}

	public void fixSpecialtyType(){
		if(!isSpecialty() && !name.contains(FIX_NAME))
			setName(FIX_NAME.concat(getName()));
	}
}
