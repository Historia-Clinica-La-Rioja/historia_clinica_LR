package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "observation_vital_sign")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ObservationRiskFactor extends ClinicalObservation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;
	private static final String RISK_FACTOR = "61746007";
	
	@Column(name = "loinc_code", length = 20)
	private String loincCode;

	public ObservationRiskFactor(Integer patientId, String value, Integer snomedId,
								 String cie10Codes, ERiskFactor eRiskFactor, LocalDateTime effectiveTime){
		super(patientId, value, snomedId, cie10Codes, RISK_FACTOR, effectiveTime);
		this.loincCode = eRiskFactor.getLoincCode();
	}

	@Override
	public int hashCode() {
		return Objects.hash(loincCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObservationRiskFactor other = (ObservationRiskFactor) obj;
		return Objects.equals(loincCode, other.getLoincCode());
	}

}
