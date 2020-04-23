package net.pladema.internation.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "observation_vital_sign")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ObservationVitalSign extends InternationAuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;
	private static final String VITAL_SIGN = "61746007";
	private static final String VITAL_SIGN_LOINC = "85353-1";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "loinc_code", length = 20, nullable = true)
	private String loincCode;

	@Column(name = "sctid_code", length = 20, nullable = true)
	private String sctidCode;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId;

	@Column(name = "categoryId", length = 20, nullable = false)
	private String categoryId;

	@Column(name = "value", length = 20, nullable = false)
	private String value;

	@Column(name = "effective_time", nullable = false)
	private LocalDateTime effectiveTime;

	@Column(name = "note_id")
	private Long noteId;

	public ObservationVitalSign(Integer patientId, String value, EVitalSign evitalSign, Boolean deleted){
		this.patientId = patientId;
		this.statusId = ObservationStatus.FINAL;
		if (deleted)
			this.statusId = ObservationStatus.ERROR;
		this.categoryId = VITAL_SIGN;
		this.value = value;
		this.sctidCode = evitalSign.getSctidCode();
		this.loincCode = evitalSign.getLoincCode();
		this.effectiveTime = LocalDateTime.now();
	}

	public boolean isDeleted() {
		return this.statusId.equals(ObservationStatus.ERROR);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ObservationVitalSign that = (ObservationVitalSign) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId);
	}


}
