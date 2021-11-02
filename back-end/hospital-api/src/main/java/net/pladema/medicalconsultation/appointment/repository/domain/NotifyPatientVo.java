package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.Getter;

@Getter
public class NotifyPatientVo {
    private final Integer appointmentId;
    private final String patientLastName;
    private final String patientFirstName;
    private final Integer sectorId;
    private final String doctorFirstName;
    private final String doctorLastName;
    private final String doctorsOfficeName;
    private final String topic;

    public NotifyPatientVo(
            Integer appointmentId,
            String patientLastName,
            String patientFirstName,
            Integer sectorId,
            String doctorLastName,
            String doctorFirstName,
            String doctorsOfficeName,
            String topic) {
        this.appointmentId = appointmentId;
        this.patientLastName = patientLastName;
        this.patientFirstName = patientFirstName;
        this.sectorId = sectorId;
        this.doctorLastName = doctorLastName;
        this.doctorFirstName = doctorFirstName;
        this.doctorsOfficeName = doctorsOfficeName;
        this.topic = topic;
    }

}