package net.pladema.medicalconsultation.appointment.service.domain.notifypatient;

import lombok.Getter;

@Getter
public class NotifyPatientBo {
    private Integer appointmentId;
    private String patientName;
    private Integer sectorId;
    private String doctorName;
    private String doctorsOfficeName;
    private String topic;

    public NotifyPatientBo(Integer appointmentId, String patientName, Integer sectorId, String doctorName, String doctorsOfficeName, String topic) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.sectorId = sectorId;
        this.doctorName = doctorName;
        this.doctorsOfficeName = doctorsOfficeName;
        this.topic = topic;
    }

}
