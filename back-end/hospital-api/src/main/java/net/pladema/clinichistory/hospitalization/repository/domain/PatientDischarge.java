package net.pladema.clinichistory.hospitalization.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditListener;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient_discharge")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PatientDischarge extends SGXAuditableEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5213390289124751335L;

	@Id
	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "administrative_discharge_date")
	private LocalDate administrativeDischargeDate;

	@Column(name = "medical_discharge_date")
	private LocalDate medicalDischargeDate;

	@Column(name = "discharge_type_id", nullable = false)
	private Short dischargeTypeId;

	public PatientDischarge(PatientDischargeBo patientDischargeBo){
		this.internmentEpisodeId = patientDischargeBo.getInternmentEpisodeId();
		this.administrativeDischargeDate = patientDischargeBo.getAdministrativeDischargeDate();
		this.medicalDischargeDate = patientDischargeBo.getMedicalDischargeDate();
		this.dischargeTypeId = patientDischargeBo.getDischargeTypeId();
	}

}
