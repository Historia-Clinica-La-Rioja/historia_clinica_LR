package net.pladema.internation.repository.ips.entity;

import lombok.*;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class ClinicalObservation extends InternationAuditableEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "sctid_code", length = 20, nullable = true)
    private String sctidCode;

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

    public ClinicalObservation(Integer patientId, String value, String sctidCode, String categoryId,
                               LocalDateTime effectiveTime) {
        super();
        this.patientId = patientId;
        this.categoryId = categoryId;
        this.value = value;
        this.sctidCode = sctidCode;
        this.effectiveTime = effectiveTime;
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
