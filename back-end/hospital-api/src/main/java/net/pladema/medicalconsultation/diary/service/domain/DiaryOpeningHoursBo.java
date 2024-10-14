package net.pladema.medicalconsultation.diary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;

import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"diaryId", "overturnCount"})
@ToString
@NoArgsConstructor
public class DiaryOpeningHoursBo {

    private Integer diaryId;

    private OpeningHoursBo openingHours;

    private Short medicalAttentionTypeId;

    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

	private Boolean protectedAppointmentsAllowed;

	private Boolean onSiteAttentionAllowed;

	private Boolean patientVirtualAttentionAllowed;

	private Boolean secondOpinionVirtualAttentionAllowed;

	private Boolean regulationProtectedAppointmentsAllowed;

	public boolean overlap(DiaryOpeningHoursBo current) {
		return openingHours.overlap(current.getOpeningHours());
	}

	public Integer getOpeningHoursId() {
		return openingHours.getId();
	}

	public boolean fitsAppointmentHere(UpdateDiaryAppointmentBo a) {
		LocalTime from = openingHours.getFrom();
		LocalTime to = openingHours.getTo();
		LocalTime appointmentLocalTime = a.getTime();
		return (appointmentLocalTime.equals(from) || appointmentLocalTime.isAfter(from))
				&& appointmentLocalTime.isBefore(to);
	}

	public void updateMeWithDiaryInformation(DiaryBo diaryBo) {
		var noCarelines = diaryBo.getCareLines().isEmpty();
		if (this.getProtectedAppointmentsAllowed() != null && this.getProtectedAppointmentsAllowed() && noCarelines)
			this.setProtectedAppointmentsAllowed(false);
		this.setDiaryId(diaryBo.getId());
	}
}
