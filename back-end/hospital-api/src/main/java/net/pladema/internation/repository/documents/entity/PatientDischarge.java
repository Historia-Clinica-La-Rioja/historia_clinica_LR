package net.pladema.internation.repository.documents.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;
import net.pladema.internation.service.internment.summary.domain.PatientDischargeBo;

@Entity
@Table(name = "patient_discharge")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PatientDischarge extends InternationAuditableEntity {


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
