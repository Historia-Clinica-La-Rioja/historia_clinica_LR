package net.pladema.clinichistory.hospitalization.repository.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HealthcareProfessionalGroupPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HealthcareProfessionalGroupPK that = (HealthcareProfessionalGroupPK) o;
		return internmentEpisodeId.equals(that.internmentEpisodeId) &&
				healthcareProfessionalId.equals(that.healthcareProfessionalId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(internmentEpisodeId, healthcareProfessionalId);
	}
}
