package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "anesthetic_technique")
@Entity
public class AnestheticTechnique {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "snomed_id", nullable = false)
    private Integer snomedId;

    @Column(name = "technique_id")
    private Short techniqueId;

    @Column(name = "tracheal_intubation")
    private Boolean trachealIntubation;

    @Column(name = "breathing_id")
    private Short breathingId;

    @Column(name = "circuit_id")
    private Short circuitId;

}
