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
@Table(name = "analgesic_technique")
@Entity
public class AnalgesicTechnique {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anesthetic_substance_id", nullable = false)
    private Integer anesthetic_substance_id;

    @Column(name = "injection_note_id")
    private Long injectionNoteId;

    @Column(name = "catheter")
    private Boolean catheter;

    @Column(name = "catheter_note_id")
    private Long catheterNoteId;
}
