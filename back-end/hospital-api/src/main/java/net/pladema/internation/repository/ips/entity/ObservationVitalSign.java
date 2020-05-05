package net.pladema.internation.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationListener;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

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
	private static final String VITAL_SIGN_LOINC = "85353-1";

	@Column(name = "loinc_code", length = 20)
	private String loincCode;

	public ObservationVitalSign(Integer patientId, String value, EVitalSign evitalSign){
		super(patientId, value, evitalSign.getSctidCode(), VITAL_SIGN);
		this.loincCode = evitalSign.getLoincCode();
	}

}
