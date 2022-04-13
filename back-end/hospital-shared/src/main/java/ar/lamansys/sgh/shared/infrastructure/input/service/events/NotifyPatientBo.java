package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotifyPatientBo {
    private final Integer appointmentId;
    private final String patientName;
    private final Integer sectorId;
    private final String doctorName;
    private final String doctorsOfficeName;
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
