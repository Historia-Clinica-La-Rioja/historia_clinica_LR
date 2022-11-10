package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "auditable_patient")
@AllArgsConstructor
@NoArgsConstructor
public class AuditablePatient {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn = LocalDateTime.now();

	@Column(name = "message", columnDefinition = "TEXT", nullable = false)
	private String message;

}
