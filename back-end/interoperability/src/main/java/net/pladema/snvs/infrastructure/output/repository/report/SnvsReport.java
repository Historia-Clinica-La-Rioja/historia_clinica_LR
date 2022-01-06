package net.pladema.snvs.infrastructure.output.repository.report;

import lombok.AllArgsConstructor;
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
@Table(name = "snvs_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnvsReport {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "group_event_id", nullable = false)
	private Integer groupEventId;

	@Column(name = "event_id", nullable = false)
	private Integer eventId;

	@Column(name = "manual_classification_id", nullable = false)
	private Integer manualClassificationId;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "snomed_sctid", length = 20)
	private String snomedSctid;

	@Column(name = "snomed_pt", nullable = false)
	private String snomedPt;

	@Column(name = "status", length = 1024, nullable = false)
	private String status;

	@Column(name = "response_code", nullable = false)
	private Short responseCode;

	@Column(name = "professional_id", nullable = false)
	private Integer professionalId;

	@Column(name = "institution_id", length = 15, nullable = false)
	private Integer institutionId;

	@Column(name = "sisa_registered_id", nullable = false)
	private Integer sisaRegisteredId;

	@Column(name = "last_update", nullable = false)
	private LocalDate lastUpdate;



}