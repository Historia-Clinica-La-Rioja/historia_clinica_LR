package net.pladema.internation.repository.core.entity;

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

	@Column(name = "discharge_date", nullable = false)
	private LocalDate dischargeDate;

	@Column(name = "discharge_type_id", nullable = false)
	private Short dischargeTypeId;

}
