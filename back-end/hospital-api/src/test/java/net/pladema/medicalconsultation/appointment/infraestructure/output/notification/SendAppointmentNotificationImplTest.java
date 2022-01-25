package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendAppointmentNotificationImplTest {

    private SendAppointmentNotificationImpl sendAppointmentNotification;

    @BeforeEach
    void setUp() {
        sendAppointmentNotification = new SendAppointmentNotificationImpl(
                new MqttCallExternalService(null)
        );
    }

    @Test
    void correctFormatting() {
        NotifyPatientBo notifyPatientBoMock = new NotifyPatientBo(
                -2,
                "MockName",
                -3,
                "MockDrName",
                "MockDrsOfficeName",
                "MockTopic"
        );

        String shouldBe = "data\":{\"appointmentId\":-2,\"patient\":\"MockName\",\"sector\":-3,\"doctor\":\"MockDrName\",\"doctorsOffice\":\"MockDrsOfficeName\"}";
        Assertions.assertEquals(sendAppointmentNotification.getMessage(notifyPatientBoMock), shouldBe);

    }
}
