package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.domain.DiaryAppointmentsSearchBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsInfoBo;

import java.util.List;

public interface DiaryAvailableAppointmentsSearchRepository {

	List<DiaryAvailableAppointmentsInfoBo> getAllDiaryAppointmentsByFilter(DiaryAppointmentsSearchBo diaryAppointmentsSearchBo);

}
