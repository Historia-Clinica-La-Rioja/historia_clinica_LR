package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppointmentStatusSummary implements Serializable {

	private static final long serialVersionUID = -6349210572359248285L;

    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 10)
    private String description;

    public AppointmentStatusSummary(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
