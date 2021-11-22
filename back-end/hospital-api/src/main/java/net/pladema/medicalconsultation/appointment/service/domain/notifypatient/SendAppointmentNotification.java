package net.pladema.medicalconsultation.appointment.service.domain.notifypatient;

public interface SendAppointmentNotification {

    void run(NotifyPatientBo notifyPatientBo);
}
