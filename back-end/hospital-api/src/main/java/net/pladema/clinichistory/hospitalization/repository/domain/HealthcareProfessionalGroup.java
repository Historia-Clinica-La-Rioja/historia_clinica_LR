package net.pladema.clinichistory.hospitalization.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "healthcare_professional_group")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthcareProfessionalGroup implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private HealthcareProfessionalGroupPK pk;

	@Column(name = "responsible")
	private Boolean responsible;

	public HealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId){
		pk = new HealthcareProfessionalGroupPK(internmentEpisodeId, healthcareProfessionalId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HealthcareProfessionalGroup that = (HealthcareProfessionalGroup) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
