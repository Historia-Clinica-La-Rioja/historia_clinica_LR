package net.pladema.medicalconsultation.appointment.service.notifypatient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.NotifyPatientVo;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;

@ExtendWith(MockitoExtension.class)
@Disabled
class NotifyPatientTest {

    private NotifyPatient notifyPatient;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MqttCallExternalService mqttCallExternalService;

    private SendAppointmentNotification sendAppointmentNotification;

    @BeforeEach
    void setUp() {
/*        sendAppointmentNotification = new SendAppointmentNotificationImpl(mqttCallExternalService);
        this.notifyPatient = new NotifyPatient(
                new AppointmentNotificationStorageImpl(appointmentRepository),
                sendAppointmentNotification
        );*/
    }

    @Test
    void success() {
        var notifyPatientVo = new NotifyPatientVo(1, "PATIENT_LASTNAME", "PATIENT_FIRSTNAME", 1,
                "DOCTOR_LASTNAME", "DOCTOR_FIRSTNAME", "OFFICE_NAME", "TOPIC");
        when(appointmentRepository.getNotificationData(any())).thenReturn(Optional.of(notifyPatientVo));
        notifyPatient.run(456, -742);

        ArgumentCaptor<MqttMetadataDto> mqttMetadataDtoArgCaptor = ArgumentCaptor.forClass(MqttMetadataDto.class);
        verify(mqttCallExternalService, times(1)).publish(mqttMetadataDtoArgCaptor.capture());
        assertThat(mqttMetadataDtoArgCaptor.getValue().getTopic()).isEqualTo("TOPIC");
        assertThat(mqttMetadataDtoArgCaptor.getValue().getMessage()).isEqualTo("{\"type\":\"add\",\"data\":{\"appointmentId\":1,\"patient\":\"PATIENT_LASTNAME, PATIENT_FIRSTNAME\",\"sector\":1,\"doctor\":\"DOCTOR_LASTNAME, DOCTOR_FIRSTNAME\",\"doctorsOffice\":\"OFFICE_NAME\"}");
        assertThat(mqttMetadataDtoArgCaptor.getValue().isRetained()).isTrue();
        assertThat(mqttMetadataDtoArgCaptor.getValue().getQos()).isEqualTo(2);
    }
}
