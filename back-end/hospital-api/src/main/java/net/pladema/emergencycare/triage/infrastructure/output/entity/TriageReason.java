package net.pladema.emergencycare.triage.infrastructure.output.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Table(name="triage_reason")
@Entity
public class TriageReason implements Serializable {

	private static final long serialVersionUID = 8591742176473852849L;

	@EmbeddedId
	private TriageReasonPk pk;

	public TriageReason(Integer triageId ,String reasonId){
		pk = new TriageReasonPk(triageId,reasonId);
	}
}
