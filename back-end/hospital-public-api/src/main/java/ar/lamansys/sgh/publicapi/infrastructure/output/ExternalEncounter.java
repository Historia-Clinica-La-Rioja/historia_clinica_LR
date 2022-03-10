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
@Table(name = "external_encounter")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalEncounter {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "external_encounter_id")
    private String externalEncounterId;

    @Column(name = "external_encounter_date")
    private LocalDateTime externalEncounterDate;

    @Column(name = "external_encounter_type")
    private String externalEncounterType;

    @Column(name = "institution_id")
    private Integer institutionId;
}
