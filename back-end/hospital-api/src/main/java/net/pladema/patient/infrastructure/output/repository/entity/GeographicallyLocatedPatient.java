package net.pladema.patient.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "geographically_located_patient")
@Entity
public class GeographicallyLocatedPatient implements Serializable {

	private static final long serialVersionUID = -6318816698939607013L;

	@Id
	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "status_id")
	private Short statusId;

}
