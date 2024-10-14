package net.pladema.imagenetwork.derivedstudies.service.domain;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResultStudiesBO {

	private Integer id;

	private Integer appointmentId;

	private Integer idMove;

	private String patientId;

	private String patientName;

	private Date studyDate;

	private Time studyTime;

	private String modality;

	private String studyInstanceUid;

	private Date auditDate;
}
