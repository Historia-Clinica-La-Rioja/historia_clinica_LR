package net.pladema.emergencycare.triage.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class TriageRiskFactorsPk implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3698692565260620917L;

	@Column(name = "triage_id", nullable = false)
	private Integer triageId;

	@Column(name = "observation_vital_sign_id", nullable = false)
	private Integer observationRiskFactorId;
}
