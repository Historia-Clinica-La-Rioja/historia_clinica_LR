package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "snomed_synonym")
@Getter
@Setter
@NoArgsConstructor
public class SnomedSynonym implements Serializable {

	@EmbeddedId
	private SnomedSynonymPK pk;

	public SnomedSynonym(Integer mainConceptId, Integer synonymId){
		pk = new SnomedSynonymPK(mainConceptId, synonymId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnomedSynonym that = (SnomedSynonym) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}

}
