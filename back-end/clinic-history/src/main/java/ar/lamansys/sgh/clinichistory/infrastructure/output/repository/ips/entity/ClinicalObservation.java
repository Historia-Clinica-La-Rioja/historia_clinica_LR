package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class ClinicalObservation extends SGXAuditableEntity<Integer> implements SGXDocumentEntity {

    private static final long serialVersionUID = -3877133949925317197L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "snomed_id", nullable = false)
    private Integer snomedId;

    @Column(name = "cie10_codes", length = 255, nullable = true)
    private String cie10Codes;

    @Column(name = "status_id", length = 20, nullable = false)
    private String statusId = ObservationStatus.FINAL;

    @Column(name = "categoryId", length = 20, nullable = false)
    private String categoryId;

    @Column(name = "value", length = 20, nullable = false)
    private String value;

    @Column(name = "effective_time", nullable = false)
    private LocalDateTime effectiveTime;

    @Column(name = "note_id")
    private Long noteId;

    public ClinicalObservation(Integer patientId, String value, Integer snomedId, String cie10Codes, String categoryId,
                               LocalDateTime effectiveTime, String statusId) {
        super();
        this.patientId = patientId;
        this.categoryId = categoryId;
        this.value = value;
        this.snomedId = snomedId;
        this.cie10Codes = cie10Codes;
        this.effectiveTime = effectiveTime;
        if (statusId != null)
            this.statusId = statusId;
    }

    public ClinicalObservation(Integer patientId, String value, Integer snomedId, String categoryId,
                               LocalDateTime effectiveTime, String statusId) {
        super();
        this.patientId = patientId;
        this.categoryId = categoryId;
        this.value = value;
        this.snomedId = snomedId;
        this.effectiveTime = effectiveTime;
        if (statusId != null)
            this.statusId = statusId;
    }

    public boolean isDeleted() {
        return this.statusId.equals(ObservationStatus.ERROR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicalObservation that = (ClinicalObservation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
