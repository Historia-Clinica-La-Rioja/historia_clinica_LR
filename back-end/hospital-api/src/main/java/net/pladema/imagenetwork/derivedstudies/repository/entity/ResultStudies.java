package net.pladema.imagenetwork.derivedstudies.repository.entity;

import lombok.*;

import javax.persistence.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "result_studies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class ResultStudies {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "appointment_id")
	private Integer appointmentId;

	@Column(name = "id_move")
	private Integer idMove;

	@Column(name = "patient_id")
	private String patientId;

	@Column(name = "patient_name")
	private String patientName;

	@Column(name = "study_date")
	private Date studyDate;

	@Column(name = "study_time")
	private Time studyTime;

	@Column(name = "modality")
	private String modality;

	@Column(name = "study_instance_uid")
	private String studyInstanceUid;

	@Column(name = "audit_date")
	private Date auditDate;

	public  ResultStudies (Integer appointmentId, Integer idMove, String patientId, String patientName, Date studyDate, Time studyTime, String modality, String studyInstanceUid, Date auditDate){
		this.appointmentId = appointmentId;
		this.idMove =idMove;
		this.patientId = patientId;
		this.patientName = patientName;
		this.studyDate = studyDate;
		this.studyTime = studyTime;
		this.modality = modality;
		this.studyInstanceUid = studyInstanceUid;
		this.auditDate = auditDate;

	}
}
