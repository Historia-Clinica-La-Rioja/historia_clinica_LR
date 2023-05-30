package net.pladema.medicalconsultation.appointment.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "study_id")
	private Integer studyId;

	@Column(name = "image_id")
	private String imageId;

	@Column(name = "completed", nullable = false)
	private Boolean completed;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

    public AppointmentOrderImage( Integer appointmentId, Integer orderId, Integer studyId, String imageId, Boolean completed){
        this.pk = new AppointmentOrderImagePK(appointmentId);
		this.imageId = imageId;
		this.completed = completed;
		this.orderId = orderId;
		this.studyId = studyId;
    }
}
