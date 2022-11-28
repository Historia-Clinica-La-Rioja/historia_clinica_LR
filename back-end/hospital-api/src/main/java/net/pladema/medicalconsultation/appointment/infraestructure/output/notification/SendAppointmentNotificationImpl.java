package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.NotifyPatientDto;
import lombok.AllArgsConstructor;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;

@AllArgsConstructor
@Service
public class SendAppointmentNotificationImpl implements SendAppointmentNotification {

	private final HospitalApiPublisher hospitalApiPublisher;

    @Override
    public void run(NotifyPatientBo notifyPatientBo) {
		hospitalApiPublisher.appointmentCaller(mapTo(notifyPatientBo));
    }

    private NotifyPatientDto mapTo(NotifyPatientBo notifyPatientBo) {
        return new NotifyPatientDto(
                notifyPatientBo.getAppointmentId(),
				notifyPatientBo.getPatientName(),
				notifyPatientBo.getSectorId(),
				notifyPatientBo.getDoctorName(),
				notifyPatientBo.getDoctorsOfficeName(),
				notifyPatientBo.getTopic()
        );
    }
}
