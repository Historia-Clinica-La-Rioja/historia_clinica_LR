package net.pladema.medicalconsultation.appointment.repository.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "document_appointment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentAppointment implements Serializable {

	@EmbeddedId
	private DocumentAppointmentPK pk;

	public DocumentAppointment(Long documentId, Integer appointmentId){
		this.pk = new DocumentAppointmentPK(documentId, appointmentId);
	}

}
