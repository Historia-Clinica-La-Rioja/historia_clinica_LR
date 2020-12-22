package net.pladema.emergencycare.triage.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "triage_vital_signs")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class TriageVitalSigns {
	/**
	 *
	 */
	private static final long serialVersionUID = -229982705620842778L;

	@EmbeddedId
	private TriageVitalSignsPk pk;

	public TriageVitalSigns(Integer triageId, Integer observationVitalSignsId){
		pk = new TriageVitalSignsPk(triageId, observationVitalSignsId);
	}
}
