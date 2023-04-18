package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "appointment_order_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppointmentOrderImage implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 2516704317374146771L;
	@EmbeddedId
    private AppointmentOrderImagePK pk;

	@Column(name = "image_id", nullable = false)
	private String imageId;

	@Column(name = "completed", nullable = false)
	private Boolean completed;

    public AppointmentOrderImage( Integer appointmentId, Integer orderId, String imageId, Boolean completed){
        this.pk = new AppointmentOrderImagePK(appointmentId, orderId);
		this.imageId = imageId;
		this.completed = completed;
    }
}
