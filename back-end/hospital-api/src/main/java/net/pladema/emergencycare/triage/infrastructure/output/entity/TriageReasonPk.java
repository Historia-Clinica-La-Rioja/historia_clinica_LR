package net.pladema.emergencycare.triage.infrastructure.output.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TriageReasonPk implements Serializable {

	@Column(name = "triage_id", nullable = false)
	private Integer triageId;

	@Column(name = "reason_id", nullable = false)
	private String reasonId;
}
