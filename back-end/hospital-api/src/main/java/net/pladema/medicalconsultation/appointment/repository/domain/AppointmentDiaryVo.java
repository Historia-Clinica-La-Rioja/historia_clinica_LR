package net.pladema.medicalconsultation.appointment.repository.domain;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.ToString;
import lombok.Value;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryLabel;

@Value
@ToString
public class AppointmentDiaryVo {

	private final Integer id;

	private final Integer patientId;

	private final Integer diaryId;

	private final LocalDate date;

	private final LocalTime hour;

	private final Short appointmentStateId;

	private final boolean overturn;

	private final Integer patientMedicalCoverageId;

	private final Short medicalAttentionTypeId;

	private final String phonePrefix;

	private final String phoneNumber;

	private final Short appointmentBlockMotiveId;

	private final LocalDateTime updatedOn;

	private final LocalDateTime createdOn;

	private final ProfessionalPersonVo professionalPersonVo;

	private final String email;

	private final DiaryLabel diaryLabel;
	
	private final Short recurringAppointmentTypeId;

	private final Integer openingHoursId;

	public AppointmentDiaryVo(
			Integer diaryId,
			Integer id,
			Integer patientId,
			LocalDate date,
			LocalTime hour,
			Short appointmentStateId,
			boolean overturn,
			Integer patientMedicalCoverageId,
			String phonePrefix,
			String phoneNumber,
			Short medicalAttentionTypeId,
			Short appointmentBlockMotiveId,
			LocalDateTime updatedOn,
			LocalDateTime createdOn,
			Integer personId,
			String firstName,
			String lastName,
			String nameSelfDetermination,
			String middleNames,
			String otherLastName,
			String email,
			DiaryLabel diaryLabel,
			Integer openingHoursId) {
		this.diaryId = diaryId;
		this.id = id;
		this.patientId = patientId;
		this.date = date;
		this.hour = hour;
		this.appointmentStateId = appointmentStateId;
		this.overturn = overturn;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.medicalAttentionTypeId = medicalAttentionTypeId;
		this.appointmentBlockMotiveId = appointmentBlockMotiveId;
		this.updatedOn = updatedOn;
		this.createdOn = createdOn;
		this.email = email;
		this.professionalPersonVo = personId != null ? new ProfessionalPersonVo(personId, firstName, lastName, nameSelfDetermination, middleNames, otherLastName) : null;
		this.diaryLabel = diaryLabel;
		this.recurringAppointmentTypeId = null;
		this.openingHoursId = openingHoursId;
	}

}
