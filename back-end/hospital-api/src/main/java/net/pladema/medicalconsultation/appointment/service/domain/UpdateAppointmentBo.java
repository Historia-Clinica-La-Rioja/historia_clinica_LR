package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentBo {

    private Integer appointmentId;

    private Integer patientId;

    private boolean isOverturn;

    private short appointmentStateId;

    private Integer patientMedicalCoverageId;

    private String phoneNumber;
}
