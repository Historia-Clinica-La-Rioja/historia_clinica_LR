package net.pladema.cipres.infrastructure.output.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CipresPatientPk implements Serializable {

	private static final long serialVersionUID = -5189820580401108747L;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "cipres_patient_id", nullable = false)
	private Long cipresPatientId;

}