package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "reference")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Reference implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6613950355191408971L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "encounter_id", nullable = false)
    private Integer encounterId;

    @Column(name = "source_type_id", nullable = false)
    private Integer sourceTypeId;

    @Column(name = "consultation")
    private Boolean consultation;

    @Column(name = "procedure")
    private Boolean procedure;

    @Column(name = "care_line_id")
    private Integer careLineId;

    @Column(name = "clinical_specialty_id", nullable = false)
    private Integer clinicalSpecialtyId;

    @Column(name = "reference_note_id")
    private Integer referenceNoteId;

    @Column(name = "destination_institution_id", nullable = false)
	private Integer destinationInstitutionId;

    public Reference(ReferenceBo referenceBo) {
        this.encounterId = referenceBo.getEncounterId();
        this.sourceTypeId = referenceBo.getSourceTypeId();
        this.consultation = referenceBo.getConsultation();
        this.procedure = referenceBo.getProcedure();
        this.careLineId = referenceBo.getCareLineId();
        this.clinicalSpecialtyId = referenceBo.getClinicalSpecialtyId();
        this.destinationInstitutionId = referenceBo.getDestinationInstitutionId();
    }

}
