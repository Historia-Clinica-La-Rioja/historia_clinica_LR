package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EObservationLab;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "observation_lab")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ObservationLab extends ClinicalObservation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	private static final String LAB = "lab";

	public ObservationLab(Integer patientId, String value, EObservationLab eObservationLab, LocalDateTime effectiveTime){
		super(patientId, value, eObservationLab.getSctidCode(), LAB, effectiveTime);
	}

}
