package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ConsultationReasonBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "odontology_reason")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OdontologyReason {

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "description", nullable = false)
    private String description;

    public OdontologyReason(ConsultationReasonBo reason) {
        this.id = reason.getSctid();
        this.description = reason.getPt();
    }

}
