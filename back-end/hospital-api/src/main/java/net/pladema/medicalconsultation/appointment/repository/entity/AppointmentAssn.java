package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "appointment_assn")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppointmentAssn implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2516704317374146771L;
	@EmbeddedId
    private AppointmentAssnPK pk;

    public AppointmentAssn(Integer diaryId, Integer openingHoursId, Integer appointmentId){
        this.pk = new AppointmentAssnPK(diaryId, openingHoursId, appointmentId);
    }
}
