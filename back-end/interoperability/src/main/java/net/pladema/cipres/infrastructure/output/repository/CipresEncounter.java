package net.pladema.cipres.infrastructure.output.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "cipres_encounter")
@Getter
@Setter
@NoArgsConstructor
public class CipresEncounter {

	private static final long serialVersionUID = 2948718743420512823L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "encounter_id", nullable = false)
	private Integer encounterId;

	@Column(name = "encounter_api_id")
	private Integer encounterApiId;

	@Column(name = "status", columnDefinition = "TEXT")
	private String status;

	@Column(name = "response_code", nullable = false)
	private Short responseCode;

	@Column(name="date", nullable = false)
	private LocalDate date;

}