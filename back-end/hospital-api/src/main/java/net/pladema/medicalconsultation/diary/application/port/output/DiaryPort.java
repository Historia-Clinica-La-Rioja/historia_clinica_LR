package net.pladema.medicalconsultation.diary.application.port.output;

import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;

import java.util.List;

public interface DiaryPort {

	List<UpdateDiaryAppointmentBo> getUpdateDiaryAppointments(Integer diaryId);

}
