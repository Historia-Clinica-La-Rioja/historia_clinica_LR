package ar.lamansys.sgh.publicapi.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "external_patient")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPatient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "external_encounter_id")
    private String externalEncounterId;

    @Column(name = "external_encounter_date")
    private LocalDateTime externalEncounterDate;

    @Column(name = "external_encounter_type")
    private String externalEncounterType;

    public ExternalPatient(Integer patientId, String externalId, String externalEncounterId,
                           LocalDateTime externalEncounterDate, String externalEncounterType){
        this.patientId = patientId;
        this.externalId = externalId;
        this.externalEncounterId = externalEncounterId;
        this.externalEncounterDate = externalEncounterDate;
        this.externalEncounterType = externalEncounterType;
    }

}
