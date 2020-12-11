package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EVitalSign;

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
public class ObservationVitalSign extends ClinicalObservation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;
	private static final String VITAL_SIGN = "61746007";
	
	@Column(name = "loinc_code", length = 20)
	private String loincCode;

	public ObservationVitalSign(Integer patientId, String value, Integer snomedId, EVitalSign evitalSign, LocalDateTime effectiveTime){
		super(patientId, value, snomedId, VITAL_SIGN, effectiveTime);
		this.loincCode = evitalSign.getLoincCode();
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
		ObservationVitalSign other = (ObservationVitalSign) obj;
		return Objects.equals(loincCode, other.getLoincCode());
	}

}
