package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@Table(name = "reference_appointment")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceAppointment extends SGXAuditableEntity<ReferenceAppointmentPk> {

	@EmbeddedId
	public ReferenceAppointmentPk pk;

	public ReferenceAppointment(Integer referenceId, Integer appointmentId) {
		this.pk = new ReferenceAppointmentPk(referenceId, appointmentId);
	}

	@Override
	public ReferenceAppointmentPk getId() {
		return this.pk;
	}
}
