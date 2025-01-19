package net.pladema.emergencycare.triage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TriageReasonBo {

	private Integer triageId;
	private String reasonId;
}
