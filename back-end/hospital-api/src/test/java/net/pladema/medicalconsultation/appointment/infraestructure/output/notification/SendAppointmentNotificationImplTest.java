package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.NotifyPatientDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;

@ExtendWith(MockitoExtension.class)
class SendAppointmentNotificationImplTest {

    private SendAppointmentNotificationImpl sendAppointmentNotification;

	@Mock
	private HospitalApiPublisher hospitalApiPublisher;

    @BeforeEach
    void setUp() {
        sendAppointmentNotification = new SendAppointmentNotificationImpl(hospitalApiPublisher);
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
		ArgumentCaptor<NotifyPatientDto> valueCapture = ArgumentCaptor.forClass(NotifyPatientDto.class);
		sendAppointmentNotification.run(notifyPatientBoMock);
		verify(hospitalApiPublisher, times(1)).appointmentCaller(valueCapture.capture());
		Assertions.assertEquals(notifyPatientBoMock.getAppointmentId(), valueCapture.getValue().getAppointmentId());
		Assertions.assertEquals(notifyPatientBoMock.getPatientName(), valueCapture.getValue().getPatientName());
		Assertions.assertEquals(notifyPatientBoMock.getDoctorName(), valueCapture.getValue().getDoctorName());
		Assertions.assertEquals(notifyPatientBoMock.getDoctorsOfficeName(), valueCapture.getValue().getDoctorsOfficeName());
		Assertions.assertEquals(notifyPatientBoMock.getTopic(), valueCapture.getValue().getTopic());
		Assertions.assertEquals(notifyPatientBoMock.getSectorId(), valueCapture.getValue().getSectorId());
    }
}
