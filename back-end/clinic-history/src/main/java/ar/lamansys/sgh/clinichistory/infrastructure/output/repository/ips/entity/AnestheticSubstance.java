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
@Table(name = "anesthetic_substance")
@Entity
public class AnestheticSubstance {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "snomed_id", nullable = false)
    private Integer snomedId;

    @Column(name = "dosage_id")
    private Integer dosageId;

    @Column(name = "via_id")
    private Short viaId;

    @Column(name = "via_note_id")
    private Long viaNoteId;

    @Column(name = "type_id", nullable = false)
    private Short typeId;
}
