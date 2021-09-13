package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "patient_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientAudit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5166666629123947441L;

    @EmbeddedId
    private PatientAuditPK pk;

    public PatientAudit(Integer patientId, Integer hospitalAuditId){
        pk = new PatientAuditPK(patientId, hospitalAuditId);
    }

}