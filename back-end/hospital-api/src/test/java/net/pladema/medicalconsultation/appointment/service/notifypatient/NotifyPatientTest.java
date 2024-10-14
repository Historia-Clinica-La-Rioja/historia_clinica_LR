package net.pladema.medicalconsultation.appointment.service.notifypatient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.AppointmentNotificationStorageImpl;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.NotifyPatientVo;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;
import net.pladema.medicalconsultation.appointment.service.exceptions.NotifyPatientException;

@ExtendWith(MockitoExtension.class)
class NotifyPatientTest {

    private NotifyPatient notifyPatient;

    @Mock
    private AppointmentRepository appointmentRepository;

	@Mock
    private SendAppointmentNotification sendAppointmentNotification;

    @BeforeEach
    void setUp() {

        this.notifyPatient = new NotifyPatient(
                new AppointmentNotificationStorageImpl(appointmentRepository),
                sendAppointmentNotification
        );
    }

	@Test
	void run_success() throws NotifyPatientException {
		when(appointmentRepository.getNotificationData(any())).thenReturn(Optional.of(newNotifyPatientVo(1, "topic")));

		this.notifyPatient.run(1,1);
	}

	@Test
	void run_failsNoData() throws NotifyPatientException {
		when(appointmentRepository.getNotificationData(any())).thenReturn(Optional.empty());

		var exception = notifyPatientWithError(1, 1);
		assertThat(exception.sectorId).isNull();

	}

	@Test
	void run_failsNoTopic() throws NotifyPatientException {
		when(appointmentRepository.getNotificationData(any())).thenReturn(Optional.of(newNotifyPatientVo(11, null)));

		var exception = notifyPatientWithError(1, 1);
		assertThat(exception.sectorId).isEqualTo(11);

	}

	private NotifyPatientException notifyPatientWithError(Integer institutionId, Integer appointmentId) throws NotifyPatientException {
		NotifyPatientException exception = Assertions.assertThrows(NotifyPatientException.class, () ->
				notifyPatient.run(institutionId,appointmentId)
		);
		return exception;
	}


	private static NotifyPatientVo newNotifyPatientVo(Integer sectorId, String topic) {
		return new NotifyPatientVo(
				1,
				"PATIENT_LASTNAME",
				"PATIENT_FIRSTNAME",
				sectorId,
                "DOCTOR_LASTNAME",
				"DOCTOR_FIRSTNAME",
				"OFFICE_NAME",
				topic
		);
	}

}
