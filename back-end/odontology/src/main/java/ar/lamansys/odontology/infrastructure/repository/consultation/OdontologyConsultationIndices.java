package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "odontology_consultation_indices")
@Getter
@Setter
@NoArgsConstructor
public class OdontologyConsultationIndices {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "odontology_consultation_id", nullable = false)
    private Integer odontologyConsultationId;

    @Column(name = "permanent_c", nullable = false)
    private Integer permanentC;

    @Column(name = "permanent_p", nullable = false)
    private Integer permanentP;

    @Column(name = "permanent_o", nullable = false)
    private Integer permanentO;

    @Column(name = "temporary_c", nullable = false)
    private Integer temporaryC;

    @Column(name = "temporary_e", nullable = false)
    private Integer temporaryE;

    @Column(name = "temporary_o", nullable = false)
    private Integer temporaryO;

    @Column(name = "permanent_teeth_present")
    private Integer permanentTeethPresent;

    @Column(name = "temporary_teeth_present")
    private Integer temporaryTeethPresent;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public OdontologyConsultationIndices(Integer odontologyConsultationId, CpoCeoIndicesBo indices) {
        this.odontologyConsultationId = odontologyConsultationId;
        this.permanentC = (indices.getPermanentC() != null) ? indices.getPermanentC() : 0;
        this.permanentP = (indices.getPermanentP() != null) ? indices.getPermanentP() : 0;
        this.permanentO = (indices.getPermanentO() != null) ? indices.getPermanentO() : 0;
        this.temporaryC = (indices.getTemporaryC() != null) ? indices.getTemporaryC() : 0;
        this.temporaryE = (indices.getTemporaryE() != null) ? indices.getTemporaryE() : 0;
        this.temporaryO = (indices.getTemporaryO() != null) ? indices.getTemporaryO() : 0;
        this.permanentTeethPresent = (indices.getPermanentTeethPresent() != null) ? indices.getPermanentTeethPresent() : null;
        this.temporaryTeethPresent = (indices.getTemporaryTeethPresent() != null) ? indices.getTemporaryTeethPresent() : null;
        this.date = indices.getConsultationDate();
    }

}
