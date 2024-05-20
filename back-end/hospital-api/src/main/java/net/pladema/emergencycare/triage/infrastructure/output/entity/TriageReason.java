package net.pladema.emergencycare.triage.infrastructure.output.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Table(name="triage_reason")
@Entity
public class TriageReason {

	@EmbeddedId
	private TriageReasonPk pk;

	public TriageReason(Integer triageId ,String reasonId){
		pk = new TriageReasonPk(triageId,reasonId);
	}
}
