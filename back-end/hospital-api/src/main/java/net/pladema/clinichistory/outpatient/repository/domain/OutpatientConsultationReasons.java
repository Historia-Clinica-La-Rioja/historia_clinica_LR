package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "outpatient_consultation_reasons")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OutpatientConsultationReasons implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private OutpatientConsultationReasonsPK pk;

	public OutpatientConsultationReasons(Integer outpatientConsultationId, String reasonId){
		pk = new OutpatientConsultationReasonsPK(outpatientConsultationId, reasonId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OutpatientConsultationReasons that = (OutpatientConsultationReasons) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
