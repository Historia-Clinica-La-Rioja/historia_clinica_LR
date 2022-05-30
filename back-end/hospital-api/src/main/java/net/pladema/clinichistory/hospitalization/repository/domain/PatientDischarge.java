package net.pladema.clinichistory.hospitalization.repository.domain;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_discharge")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PatientDischarge extends SGXAuditableEntity<Integer> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5213390289124751335L;

	@Id
	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "administrative_discharge_date")
	private LocalDateTime administrativeDischargeDate;

	@Column(name = "medical_discharge_date")
	private LocalDateTime medicalDischargeDate;

	@Column(name = "discharge_type_id", nullable = false)
	private Short dischargeTypeId;

	@Column(name = "physical_discharge_date")
	private LocalDateTime physicalDischargeDate;

	public PatientDischarge(PatientDischargeBo patientDischargeBo){
		this.internmentEpisodeId = patientDischargeBo.getInternmentEpisodeId();
		this.administrativeDischargeDate = patientDischargeBo.getAdministrativeDischargeDate();
		this.medicalDischargeDate = patientDischargeBo.getMedicalDischargeDate();
		this.dischargeTypeId = patientDischargeBo.getDischargeTypeId();
		this.physicalDischargeDate = patientDischargeBo.getPhysicalDischargeDate();
	}

	public Integer getId() {
		return this.internmentEpisodeId;
	}
}
