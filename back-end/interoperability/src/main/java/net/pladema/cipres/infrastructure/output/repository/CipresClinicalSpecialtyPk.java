package net.pladema.cipres.infrastructure.output.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CipresClinicalSpecialtyPk implements Serializable {

	private static final long serialVersionUID = 4002621411976353572L;

	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;

	@Column(name = "cipres_clinical_specialty_id")
	private Integer cipresClinicalSpecialtyId;

}
