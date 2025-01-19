package net.pladema.medicalconsultation.diary.application.port.output;

import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import java.util.List;
import java.util.Optional;

public interface DiaryPort {

	List<UpdateDiaryAppointmentBo> getUpdateDiaryAppointments(Integer diaryId);

	Optional<DiaryBo> findById(Integer diaryId);

	Optional<Integer> findDoctorsOfficeByDiaryId(Integer diaryId);

	void deleteDiaryLabels(Integer diaryId, List<Integer> ids);
}
