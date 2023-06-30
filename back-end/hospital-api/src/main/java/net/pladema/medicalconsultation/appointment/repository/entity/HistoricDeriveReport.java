package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "historic_derive_report")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HistoricDeriveReport implements Serializable {

    @EmbeddedId
    private HistoricDeriveReportPK pk;

    @Column(name = "dest_institution_id", nullable = false)
    private Integer destInstitutionId;

    public HistoricDeriveReport(Integer appointmentId, Integer destInstitutionId){
        this.pk = new HistoricDeriveReportPK(appointmentId, LocalDateTime.now());
        this.destInstitutionId = destInstitutionId;
    }

    public HistoricDeriveReportPK getId(){
        return this.pk;
    }
}
