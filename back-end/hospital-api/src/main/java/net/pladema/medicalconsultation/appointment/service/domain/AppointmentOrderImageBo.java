package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOrderImageBo {

    private Integer appointmentId;

    private Integer orderId;

	private Integer studyId;

    private boolean completed;

    private String imageId;
}
