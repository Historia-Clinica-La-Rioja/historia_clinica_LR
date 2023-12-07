package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HistoricDeriveReportPK implements Serializable {

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Column(name = "derived_on", nullable = false)
    private LocalDateTime derivedOn;
}
