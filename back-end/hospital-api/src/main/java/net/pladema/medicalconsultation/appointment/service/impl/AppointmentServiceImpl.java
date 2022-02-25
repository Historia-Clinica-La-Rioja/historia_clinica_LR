package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import net.pladema.medicalconsultation.appointment.controller.dto.AssignedAppointmentDto;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);
	private static final String OUTPUT = "Output -> {}";

	private final AppointmentRepository appointmentRepository;

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	private final SharedStaffPort sharedStaffPort;

	public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
								  HistoricAppointmentStateRepository historicAppointmentStateRepository,
								  SharedStaffPort sharedStaffPort) {
		this.appointmentRepository = appointmentRepository;
		this.historicAppointmentStateRepository = historicAppointmentStateRepository;
		this.sharedStaffPort = sharedStaffPort;
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds) {
		LOG.debug("Input parameters -> diaryIds {}", diaryIds);
		Collection<AppointmentBo> result = new ArrayList<>();
		if (!diaryIds.isEmpty())
			result = appointmentRepository.getAppointmentsByDiaries(diaryIds).stream().map(AppointmentBo::new)
					.collect(Collectors.toList());
		LOG.debug("Result size {}", result.size());
		LOG.trace(OUTPUT, result);
		return result;
	}


	@Override
	public Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId) {
		LOG.debug("Input parameters -> diaryId {}", diaryId);
		Collection<AppointmentBo> result = appointmentRepository.getFutureActiveAppointmentsByDiary(diaryId).stream()
				.map(AppointmentBo::new).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
    public boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour) {
        LOG.debug("Input parameters -> diaryId {}, openingHoursId {}, date {}, hour {}", diaryId, openingHoursId, date, hour);
        boolean result = appointmentRepository.existAppointment(diaryId, openingHoursId, date, hour);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason) {
        LOG.debug("Input parameters -> appointmentId {}, appointmentStateId {}, userId {}, reason {}", appointmentId, appointmentStateId, userId, reason);
        appointmentRepository.updateState(appointmentId, appointmentStateId, userId);
        historicAppointmentStateRepository.save(new HistoricAppointmentState(appointmentId, appointmentStateId, reason));
        LOG.debug(OUTPUT, Boolean.TRUE);
        return Boolean.TRUE;
    }

	@Override
	public Optional<AppointmentBo> getAppointment(Integer appointmentId) {
		LOG.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = appointmentRepository.getAppointment(appointmentId).map(AppointmentBo::new);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date) {
		LOG.debug("Input parameters -> patientId {}, healthProfessionalId {}, date {} ", patientId, healthProfessionalId, date);
		boolean result = !(appointmentRepository.getAppointmentsId(patientId, healthProfessionalId, date).isEmpty());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId, LocalDate date) {
		LOG.debug("Input parameters -> patientId {}, healthcareProfessionalId {}", patientId, healthcareProfessionalId);
		List<Integer> result = appointmentRepository.getAppointmentsId(patientId, healthcareProfessionalId, date);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean updatePhoneNumber(Integer appointmentId, String phonePrefix, String phoneNumber, Integer userId) {
		appointmentRepository.updatePhoneNumber(appointmentId,phonePrefix,phoneNumber,userId);
		LOG.debug(OUTPUT, Boolean.TRUE);
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
		LOG.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId,
											LocalDate currentDate) {
		List<Integer> medicalCoverages = appointmentRepository.getMedicalCoverage(patientId, currentDate,
				AppointmentState.CONFIRMED, healthcareProfessionalId);
		Integer patientMedicalCoverageId = medicalCoverages.stream().findAny().orElse(null);
		LOG.debug(OUTPUT, patientMedicalCoverageId);
		return patientMedicalCoverageId;
	}

	private Collection<AppointmentAssignedBo> getAssignedAppointmentsByPatient(Integer patientId) {
		LOG.debug("Input parameters -> patientId {}", patientId);
		Collection<AppointmentAssignedBo> result;
		result = appointmentRepository.getAssignedAppointmentsByPatient(patientId).stream().map(AppointmentAssignedBo::new)
				.collect(Collectors.toList());
		System.out.println(result);
		LOG.debug("Result size {}", result.size());
		LOG.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId){
		LOG.debug("Input parameters -> patientId {}", patientId);
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
		LOG.debug("Result size {}", result.size());
		LOG.trace(OUTPUT, result);
		return result;
	}


}
