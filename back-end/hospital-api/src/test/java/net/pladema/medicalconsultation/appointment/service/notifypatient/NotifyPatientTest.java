package net.pladema.medicalconsultation.appointment.service.notifypatient;

import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import net.pladema.medicalconsultation.appointment.infraestructure.output.notification.AppointmentNotificationStorageImpl;
import net.pladema.medicalconsultation.appointment.infraestructure.output.notification.SendAppointmentNotificationImpl;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotifyPatientTest {

    private NotifyPatient notifyPatient;

    private final AppointmentNotificationStorage appointmentNotificationStorage;

    private final AppointmentRepository appointmentRepository;

    @Mock
    private MqttCallExternalService mqttCallExternalService;

    NotifyPatientTest(AppointmentNotificationStorage appointmentNotificationStorage, AppointmentRepository appointmentRepository) {
        this.appointmentNotificationStorage = appointmentNotificationStorage;
        this.appointmentRepository = appointmentRepository;
    }

    @BeforeEach
    void setUp() {
        this.notifyPatient = new NotifyPatient(
                new AppointmentNotificationStorageImpl(appointmentRepository),
                new SendAppointmentNotificationImpl(mqttCallExternalService)
        );
    }

    @Test
    void success() {
        notifyPatient.run(456, -742);
    }
}