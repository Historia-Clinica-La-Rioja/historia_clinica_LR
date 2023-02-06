package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.*;

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
public class EquipmentAppointmentAssnPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8256217775194750334L;

	@Column(name = "equipment_diary_id", nullable = false)
    private Integer equipmentDiaryId;

    @Column(name = "opening_hours_id", nullable = false)
    private Integer openingHoursId;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;
}
