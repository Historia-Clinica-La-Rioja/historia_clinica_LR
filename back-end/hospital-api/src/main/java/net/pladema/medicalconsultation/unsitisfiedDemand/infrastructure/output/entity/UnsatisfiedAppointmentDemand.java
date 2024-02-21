package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Table(name = "unsatisfied_appointment_demand")
@Entity
public class UnsatisfiedAppointmentDemand extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = -459272718147484465L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "clinical_specialty_name_or_alias")
	private String clinicalSpecialtyNameOrAlias;

	@Column(name = "initial_search_time")
	private LocalTime initialSearchTime;

	@Column(name = "end_search_time")
	private LocalTime endSearchTime;

	@Column(name = "initial_search_date")
	private LocalDate initialSearchDate;

	@Column(name = "end_search_date")
	private LocalDate endSearchDate;

	@Column(name = "modality_id")
	private Short modalityId;

	@Column(name = "practice_id")
	private Integer practiceId;

	@Column(name = "institution_id")
	private Integer institutionId;

}
