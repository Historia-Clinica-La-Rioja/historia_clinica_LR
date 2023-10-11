package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.service.GetPatientAppointmentHistoryService;

import net.pladema.medicalconsultation.appointment.service.domain.PatientAppointmentHistoryBo;

import net.pladema.medicalconsultation.diary.service.DiaryPracticeService;
import net.pladema.person.service.PersonService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GetPatientAppointmentHistoryServiceImpl implements GetPatientAppointmentHistoryService {

	private AppointmentRepository appointmentRepository;

	private PersonService personService;

	private DiaryPracticeService diaryPracticeService;

	@Override
	public Page<PatientAppointmentHistoryBo> run(Integer patientId, Pageable pageable) {
		log.debug("Input parameters -> patientId {}, pageable {}", patientId, pageable);
		Page<PatientAppointmentHistoryBo> result = appointmentRepository.getPatientHistory(patientId, pageable);
		result.forEach(historicData -> {
			historicData.setDoctorName(personService.getCompletePersonNameById(historicData.getDoctorPersonId()));
			historicData.setPractices(diaryPracticeService.getAllByDiaryId(historicData.getDiaryId()));
		});
		log.debug("Output -> {}", result);
		return result;
	}

}
