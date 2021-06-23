package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

	public ObservationLab(Integer patientId, String value, Integer snomedId, String cie10Codes, LocalDateTime effectiveTime){
		super(patientId, value, snomedId, cie10Codes, LAB, effectiveTime);
	}

}
