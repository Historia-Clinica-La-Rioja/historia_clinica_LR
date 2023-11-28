package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryProtectedAppointmentsSearch;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsBo;

import java.util.List;

public interface DiaryAvailableAppointmentsService {

	List<DiaryAvailableProtectedAppointmentsBo> getAvailableProtectedAppointmentsBySearchCriteria(DiaryProtectedAppointmentsSearch diaryProtectedAppointmentsSearch, Integer institutionId);

	Integer getAvailableAppointmentsBySearchCriteriaQuantity(Integer institutionDestinationId, List<Integer> clinicalSpecialtyIds, AppointmentSearchBo searchCriteria);

	Integer getAvailableAppointmentsQuantityByCareLineDiaries(Integer institutionId, List<Integer> clinicalSpecialtyIds, AppointmentSearchBo searchCriteria, Integer careLineId);

}
