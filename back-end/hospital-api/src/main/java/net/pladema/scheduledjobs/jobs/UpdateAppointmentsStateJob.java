package net.pladema.scheduledjobs.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.updateappointmentsstate.enabled",
		havingValue = "true",
		matchIfMissing = false)
public class UpdateAppointmentsStateJob {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateAppointmentsStateJob.class);

	private static final Integer ANONYMOUS_USER_ID = -1;

	private static final List<Short> STATES = Stream.of(AppointmentState.CONFIRMED, AppointmentState.ASSIGNED).collect(Collectors.toList());

	private static final String REASON = "Actualización programada";

	@Value("${scheduledjobs.updateappointmentsstate.pastdays:1}")
	private Long PAST_DAYS;

	@Value("${scheduledjobs.updateappointmentsstate.limit:10}")
	private Short LIMIT;

	private final AppointmentService appointmentService;

	@Scheduled(cron =
			"${scheduledjobs.updateappointmentsstate.seconds} " +
			"${scheduledjobs.updateappointmentsstate.minutes} " +
			"${scheduledjobs.updateappointmentsstate.hours} " +
			"${scheduledjobs.updateappointmentsstate.dayofmonth} " +
			"${scheduledjobs.updateappointmentsstate.month} " +
			"${scheduledjobs.updateappointmentsstate.dayofweek}")
	public void execute(){
		LOG.debug("Executing UpdateAppointmentsStateJob at {}", new Date());
		LocalDateTime actualDate = LocalDateTime.now();
		List<Integer> appointmentIds = appointmentService.getAppointmentsBeforeDateByStates(STATES, actualDate.minusDays(PAST_DAYS), LIMIT);
		appointmentIds.forEach(id ->
		{
			try{
				appointmentService.updateState(id, AppointmentState.ABSENT, ANONYMOUS_USER_ID, REASON);
				LOG.debug(String.format("Appointment with id %s updated successfully", id));
			}
			catch (Exception ex){
				LOG.error("Exception occurred while updating appointment: " + id.toString(), ex);
			}
		});
		LOG.debug("Finishing UpdateAppointmentsStateJob at {}", new Date());
	}

}
