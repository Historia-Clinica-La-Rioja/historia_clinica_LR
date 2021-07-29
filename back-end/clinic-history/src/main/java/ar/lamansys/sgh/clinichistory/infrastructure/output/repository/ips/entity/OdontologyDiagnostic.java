package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "odontology_diagnostic")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OdontologyDiagnostic extends SGXAuditableEntity<Integer>  {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "snomed_id", nullable = false)
    private Integer snomedId;

    @Column(name = "tooth_id")
    private Integer toothId;

    @Column(name = "surface_id")
    private Integer surfaceId;

    @Column(name = "cie10_codes")
    private String cie10Codes;

    @Column(name = "performed_date")
    private LocalDate performedDate = LocalDate.now();

    @Column(name = "note_id")
    private Long noteId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OdontologyDiagnostic that = (OdontologyDiagnostic) o;
        return id.equals(that.id) &&
                patientId.equals(that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId);
    }

}
