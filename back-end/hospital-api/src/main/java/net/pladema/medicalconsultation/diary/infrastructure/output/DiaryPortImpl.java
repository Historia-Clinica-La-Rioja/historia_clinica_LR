package net.pladema.medicalconsultation.diary.infrastructure.output;

import lombok.RequiredArgsConstructor;

import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;

import net.pladema.medicalconsultation.diary.repository.DiaryLabelRepository;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;

import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiaryPortImpl implements DiaryPort {

	private final DiaryRepository diaryRepository;
	private final DiaryLabelRepository diaryLabelRepository;

	@Override
	public List<UpdateDiaryAppointmentBo> getUpdateDiaryAppointments(Integer diaryId) {
		Short APPOINTMENT_CANCELLED_STATE_ID = 4;
		return diaryRepository.fetchUpdateDiaryAppointments(diaryId, APPOINTMENT_CANCELLED_STATE_ID);
	}

	@Override
	public Optional<DiaryBo> findById(Integer diaryId) {
		return diaryRepository.findById(diaryId)
				.map(this::mapToBo);
	}

	@Override
	public Optional<Integer> findDoctorsOfficeByDiaryId(Integer diaryId) {
		return diaryRepository.findDoctorsOfficeByDiaryId(diaryId);
	}

	private DiaryBo mapToBo(Diary diaryEntity) {
		DiaryBo diaryBo = new DiaryBo();
		diaryBo.setId(diaryEntity.getId());
		diaryBo.setHealthcareProfessionalId(diaryEntity.getHealthcareProfessionalId());
		diaryBo.setDoctorsOfficeId(diaryEntity.getDoctorsOfficeId());
		diaryBo.setStartDate(diaryEntity.getStartDate());
		diaryBo.setEndDate(diaryEntity.getEndDate());
		diaryBo.setAppointmentDuration(diaryEntity.getAppointmentDuration());
		diaryBo.setAutomaticRenewal(diaryEntity.isAutomaticRenewal());
		diaryBo.setIncludeHoliday(diaryEntity.isIncludeHoliday());
		diaryBo.setActive(true);
		diaryBo.setClinicalSpecialtyId(diaryEntity.getClinicalSpecialtyId());
		diaryBo.setAlias(diaryEntity.getAlias());
		diaryBo.setPredecessorProfessionalId(diaryEntity.getPredecessorProfessionalId());
		diaryBo.setHierarchicalUnitId(diaryEntity.getHierarchicalUnitId());
		return diaryBo;
	}

	@Override
	public void deleteDiaryLabels(Integer diaryId, List<Integer> ids) {
		diaryLabelRepository.deleteDiaryLabels(diaryId, ids);
	}

}
