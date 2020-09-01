package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
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

import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);
	private static final String OUTPUT = "Output -> {}";

	private final AppointmentRepository appointmentRepository;

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	private final DateTimeProvider dateTimeProvider;

	public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
								  HistoricAppointmentStateRepository historicAppointmentStateRepository,
								  DateTimeProvider dateTimeProvider) {
		this.appointmentRepository = appointmentRepository;
		this.historicAppointmentStateRepository = historicAppointmentStateRepository;
		this.dateTimeProvider = dateTimeProvider;
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds) {
		LOG.debug("Input parameters -> diaryIds {}", diaryIds);
		Collection<AppointmentBo> result = new ArrayList<>();
		if (!diaryIds.isEmpty())
			result = appointmentRepository.getAppointmentsByDiaries(diaryIds).stream().map(AppointmentBo::new)
					.collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
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
	public boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId) {
		LOG.debug("Input parameters -> patientId {}, healthProfessionalId {}", patientId, healthProfessionalId);
		boolean result = !(appointmentRepository.getAppointmentsId(patientId, healthProfessionalId, dateTimeProvider.nowDate()).isEmpty());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId) {
		LOG.debug("Input parameters -> patientId {}, healthcareProfessionalId {}", patientId, healthcareProfessionalId);
		List<Integer> result = appointmentRepository.getAppointmentsId(patientId, healthcareProfessionalId, dateTimeProvider.nowDate());
		LOG.debug(OUTPUT, result);
		return result;
	}


}
