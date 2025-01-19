package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.diary.domain.DiaryAppointmentsSearchBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsBo;

import java.util.List;

public interface DiaryAvailableAppointmentsService {

	List<DiaryAvailableAppointmentsBo> getAvailableProtectedAppointmentsBySearchCriteria(DiaryAppointmentsSearchBo diaryAppointmentsSearchBo, Integer institutionId);

	Integer getAvailableAppointmentsBySearchCriteriaQuantity(Integer institutionDestinationId, List<Integer> clinicalSpecialtyIds, AppointmentSearchBo searchCriteria);

	Integer getAvailableAppointmentsQuantityByCareLineDiaries(Integer institutionId, List<Integer> clinicalSpecialtyIds, AppointmentSearchBo searchCriteria, Integer careLineId);

	List<DiaryAvailableAppointmentsBo> getAvailableAppointmentsToThirdPartyBooking(DiaryAppointmentsSearchBo searchCriteria, Integer institutionId);

}
