package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.NotifyPatientVo;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentNotificationStorageImpl implements AppointmentNotificationStorage {

    private final AppointmentRepository appointmentRepository;

    public AppointmentNotificationStorageImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }


    @Override
    public Optional<NotifyPatientBo> fetchNotifyPatientData(Integer appointmentId) {
        var appointmentNotification = appointmentRepository.getNotificationData(appointmentId);
        return appointmentNotification.flatMap(this::mapTo);

    }

    private Optional<NotifyPatientBo> mapTo(NotifyPatientVo notifyPatientVo) {
        return Optional.of(new NotifyPatientBo(
                notifyPatientVo.getAppointmentId(),
                notifyPatientVo.getPatientLastName()+", "+notifyPatientVo.getPatientFirstName(),
                notifyPatientVo.getSectorId(),
                notifyPatientVo.getDoctorLastName()+", "+notifyPatientVo.getDoctorFirstName(),
                notifyPatientVo.getDoctorsOfficeName(),
                notifyPatientVo.getTopic()
        ));
    }
}
