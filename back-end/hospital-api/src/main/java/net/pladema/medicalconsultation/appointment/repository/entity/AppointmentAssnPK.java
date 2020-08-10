package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentAssnPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8256217775194750334L;

	@Column(name = "diary_id", nullable = false)
    private Integer diaryId;

    @Column(name = "opening_hours_id", nullable = false)
    private Integer openingHoursId;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;
}
