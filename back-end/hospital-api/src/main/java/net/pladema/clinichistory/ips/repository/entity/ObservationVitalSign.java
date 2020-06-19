package net.pladema.clinichistory.ips.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationListener;
import net.pladema.clinichistory.ips.service.domain.enums.EVitalSign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "observation_vital_sign")
@EntityListeners(InternationListener.class)
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

	public ObservationVitalSign(Integer patientId, String value, EVitalSign evitalSign, LocalDateTime effectiveTime){
		super(patientId, value, evitalSign.getSctidCode(), VITAL_SIGN, effectiveTime);
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
