package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
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

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "historic_clinic_history_download")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoricClinicHistoryDownload implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "download_date")
	private LocalDateTime downloadDate;

	@Column(name = "institution_id")
	private Integer institutionId;

}
