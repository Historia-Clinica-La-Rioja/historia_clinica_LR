package net.pladema.medicalconsultation.equipmentdiary.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "equipment_diary")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
public class EquipmentDiary extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "equipment_id", nullable = false)
	private Integer equipmentId;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "appointment_duration", nullable = false)
	private Short appointmentDuration;

	@Column(name = "automatic_renewal", nullable = false)
	@ColumnDefault("false")
	private boolean automaticRenewal = false;

	@Column(name = "days_before_renew", columnDefinition = "smallint default 0", nullable = false)
	private Short daysBeforeRenew = 0;


	@Column(name = "include_holiday", nullable = false)
	@ColumnDefault("false")
	private boolean includeHoliday = false;

	@Column(name = "active", nullable = false)
	@ColumnDefault("true")
	private boolean active = true;


}

