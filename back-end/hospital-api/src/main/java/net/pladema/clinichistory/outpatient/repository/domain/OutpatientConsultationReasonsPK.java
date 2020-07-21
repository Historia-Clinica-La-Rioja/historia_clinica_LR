package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OutpatientConsultationReasonsPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "outpatient_consultation_id", nullable = false)
	private Integer outpatientConsultationId;

	@Column(name = "reason_id", nullable = false)
	private String reasonID;

}
