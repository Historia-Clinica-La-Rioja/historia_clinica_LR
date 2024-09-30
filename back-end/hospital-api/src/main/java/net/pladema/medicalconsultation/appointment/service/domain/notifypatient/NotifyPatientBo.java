package net.pladema.medicalconsultation.appointment.service.domain.notifypatient;

import lombok.Getter;

@Getter
public class NotifyPatientBo {
    private final Integer appointmentId;
    private final String patientName;
    private final Integer sectorId;
    private final String doctorName;
    private final String doctorsOfficeName;
    private final String tvMonitor;

    public NotifyPatientBo(Integer appointmentId, String patientName, Integer sectorId, String doctorName, String doctorsOfficeName, String tvMonitor) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.sectorId = sectorId;
        this.doctorName = doctorName;
        this.doctorsOfficeName = doctorsOfficeName;
        this.tvMonitor = tvMonitor;
    }

	public boolean hasTVMonitor() {
		return tvMonitor != null;
	}

}
