package net.pladema.medicalconsultation.appointment.repository.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

@Entity
@Table(name = "historic_appointment_state")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HistoricAppointmentState extends SGXAuditableEntity<HistoricAppointmentStatePK> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2516704317374146771L;
	
	@EmbeddedId
    private HistoricAppointmentStatePK pk;
	
	@Column(name = "appointment_state_id", nullable = false)
    private Short appointmentStateId;
	
	@Column(name = "reason", nullable = true)
    private String reason;

    public HistoricAppointmentState(Integer appointmentId,Short appointmentStateId, String reason){
        this.pk = new HistoricAppointmentStatePK(appointmentId,LocalDateTime.now());
        this.appointmentStateId = appointmentStateId;
        this.reason = reason;
    }

    public HistoricAppointmentStatePK getId(){
        return this.pk;
    }
}
