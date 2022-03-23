package net.pladema.medicalconsultation.appointment.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentRepository appointmentRepository;

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	private final SharedStaffPort sharedStaffPort;

	public AppointmentServiceImpl(
			AppointmentRepository appointmentRepository,
			HistoricAppointmentStateRepository historicAppointmentStateRepository,
			SharedStaffPort sharedStaffPort
	) {
		this.appointmentRepository = appointmentRepository;
		this.historicAppointmentStateRepository = historicAppointmentStateRepository;
		this.sharedStaffPort = sharedStaffPort;
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds) {
		log.debug("Input parameters -> diaryIds {}", diaryIds);
		Collection<AppointmentBo> result = new ArrayList<>();
		if (!diaryIds.isEmpty())
			result = appointmentRepository.getAppointmentsByDiaries(diaryIds).stream().map(AppointmentBo::fromAppointmentDiaryVo)
					.collect(Collectors.toList());
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}


	@Override
	public Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		Collection<AppointmentBo> result = appointmentRepository.getFutureActiveAppointmentsByDiary(diaryId).stream()
				.map(AppointmentBo::fromAppointmentDiaryVo).collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
    public boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour) {
        log.debug("Input parameters -> diaryId {}, openingHoursId {}, date {}, hour {}", diaryId, openingHoursId, date, hour);
        boolean result = appointmentRepository.existAppointment(diaryId, openingHoursId, date, hour);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason) {
        log.debug("Input parameters -> appointmentId {}, appointmentStateId {}, userId {}, reason {}", appointmentId, appointmentStateId, userId, reason);
        appointmentRepository.updateState(appointmentId, appointmentStateId, userId);
        historicAppointmentStateRepository.save(new HistoricAppointmentState(appointmentId, appointmentStateId, reason));
        log.debug(OUTPUT, Boolean.TRUE);
        return Boolean.TRUE;
    }

	@Override
	public Optional<AppointmentBo> getAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = appointmentRepository.getAppointment(appointmentId).map(AppointmentBo::fromAppointmentVo);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}, date {} ", patientId, healthProfessionalId, date);
		boolean result = !(appointmentRepository.getAppointmentsId(patientId, healthProfessionalId, date).isEmpty());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}", patientId, healthcareProfessionalId);
		List<Integer> result = appointmentRepository.getAppointmentsId(patientId, healthcareProfessionalId, date);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean updatePhoneNumber(Integer appointmentId, String phonePrefix, String phoneNumber, Integer userId) {
		appointmentRepository.updatePhoneNumber(appointmentId,phonePrefix,phoneNumber,userId);
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public boolean updateMedicalCoverage(Integer appointmentId, Integer patientMedicalCoverage) {
		appointmentRepository.findById(appointmentId).ifPresent(a -> {
			if(a.isAssigned()) {
				a.setPatientMedicalCoverageId(patientMedicalCoverage);
				appointmentRepository.save(a);
			}
		});
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId,
											LocalDate currentDate) {
		List<Integer> medicalCoverages = appointmentRepository.getMedicalCoverage(patientId, currentDate,
				AppointmentState.CONFIRMED, healthcareProfessionalId);
		Integer patientMedicalCoverageId = medicalCoverages.stream().findAny().orElse(null);
		log.debug(OUTPUT, patientMedicalCoverageId);
		return patientMedicalCoverageId;
	}

	private Collection<AppointmentAssignedBo> getAssignedAppointmentsByPatient(Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		Collection<AppointmentAssignedBo> result;
		result = appointmentRepository.getAssignedAppointmentsByPatient(patientId).stream().map(AppointmentAssignedBo::new)
				.collect(Collectors.toList());
		System.out.println(result);
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId){
		log.debug("Input parameters -> patientId {}", patientId);
		Collection<AppointmentAssignedBo> resultService = this.getAssignedAppointmentsByPatient(patientId);
		Collection<AppointmentAssignedBo> result = resultService.stream()
				.parallel()
				.map(appointmentAssigned ->{
					var basicHealtcareDtoMap = sharedStaffPort.getProfessionalCompleteInfo(appointmentAssigned.getProfessionalId());
					appointmentAssigned.setSpecialties(basicHealtcareDtoMap.getClinicalSpecialties().stream()
							.map(specialty -> {return specialty.getName();})
							.collect(Collectors.toList()));
					appointmentAssigned.setProfessionalName(basicHealtcareDtoMap.getFirstName() + ' ' + basicHealtcareDtoMap.getLastName());
					return appointmentAssigned;
				}).collect(Collectors.toList());
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}


}
