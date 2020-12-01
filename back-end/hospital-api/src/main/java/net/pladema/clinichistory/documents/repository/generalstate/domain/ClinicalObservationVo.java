package net.pladema.clinichistory.documents.repository.generalstate.domain;

import lombok.*;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ObservationStatus;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EVitalSign;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalObservationVo {

	private Integer id;

	private String sctidCode;

	private String statusId;

	private String value;

	private LocalDateTime effectiveTime;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClinicalObservationVo vitalSign = (ClinicalObservationVo) o;
		return Objects.equals(id, vitalSign.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public boolean isAnthropometricData(){
		return EVitalSign.isCodeAnthropometricData(sctidCode);
	}

    public boolean hasError() {
		return statusId.equalsIgnoreCase(ObservationStatus.ERROR);
    }
}
