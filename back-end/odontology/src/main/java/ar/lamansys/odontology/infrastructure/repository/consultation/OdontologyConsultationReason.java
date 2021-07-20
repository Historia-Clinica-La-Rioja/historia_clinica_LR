package ar.lamansys.odontology.infrastructure.repository.consultation;

import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "odontology_consultation_reason")
@NoArgsConstructor
public class OdontologyConsultationReason {

    @EmbeddedId
    OdontologyConsultationReasonPK pk;

    public OdontologyConsultationReason(Integer odontologyConsultationId, String reasonId) {
        this.pk = new OdontologyConsultationReasonPK(odontologyConsultationId, reasonId);
    }

    void setOdontologyConsultationId(Integer odontologyConsultationId) {
        this.pk.setOdontologyConsultationId(odontologyConsultationId);
    }

    void setReasonId(String reasonId) {
        this.pk.setReasonId(reasonId);
    }

}
