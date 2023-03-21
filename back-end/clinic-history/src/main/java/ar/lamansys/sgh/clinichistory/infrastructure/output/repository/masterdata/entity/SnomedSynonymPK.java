package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class SnomedSynonymPK implements Serializable {

	@Column(name="main_concept_id", nullable=false)
	private Integer mainConceptId;

	@Column(name="synonym_id", nullable = false)
	private Integer synonymId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnomedSynonymPK that = (SnomedSynonymPK) o;
		return mainConceptId.equals(that.mainConceptId) &&
				synonymId.equals(that.synonymId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mainConceptId, synonymId);
	}

}
