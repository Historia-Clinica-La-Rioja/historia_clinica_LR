package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PatientAuditPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1832876231321092835L;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "hospital_audit_id", nullable = false)
    private Integer hospitalAuditId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientAuditPK that = (PatientAuditPK) o;
        return patientId.equals(that.patientId) &&
                hospitalAuditId.equals(that.hospitalAuditId);
    }

}
