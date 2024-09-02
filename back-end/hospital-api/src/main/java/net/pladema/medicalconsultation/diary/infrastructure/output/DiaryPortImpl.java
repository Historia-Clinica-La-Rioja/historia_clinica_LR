package net.pladema.medicalconsultation.diary.infrastructure.output;

import lombok.RequiredArgsConstructor;

import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;

import net.pladema.medicalconsultation.diary.repository.DiaryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaryPortImpl implements DiaryPort {

	private final DiaryRepository diaryRepository;

	@Override
	public List<UpdateDiaryAppointmentBo> getUpdateDiaryAppointments(Integer diaryId) {
		Short APPOINTMENT_CANCELLED_STATE_ID = 4;
		return diaryRepository.fetchUpdateDiaryAppointments(diaryId, APPOINTMENT_CANCELLED_STATE_ID);
	}

}
