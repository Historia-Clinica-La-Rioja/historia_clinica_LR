package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceStatus;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reference")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reference extends SGXAuditableEntity<Integer> {

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

    @Column(name = "reference_note_id")
    private Integer referenceNoteId;

    @Column(name = "destination_institution_id", nullable = false)
	private Integer destinationInstitutionId;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "phone_prefix", length = 10)
	private String phonePrefix;

	@Column(name = "priority", nullable = false)
	private Integer priority;

	@Column(name = "service_request_id")
	private Integer serviceRequestId;

	@Column(name = "regulation_state_id")
	private Short regulationStateId;
    
	@Column(name = "status_id")
	private Short statusId;

	@Column(name = "administrative_state_id")
	private Short administrativeStateId;

    public Reference(ReferenceBo referenceBo) {
        this.encounterId = referenceBo.getEncounterId();
        this.sourceTypeId = referenceBo.getSourceTypeId();
        this.consultation = referenceBo.getConsultation();
        this.procedure = referenceBo.getProcedure();
        this.careLineId = referenceBo.getCareLineId();
        this.destinationInstitutionId = referenceBo.getDestinationInstitutionId();
		this.phonePrefix = referenceBo.getPhonePrefix();
		this.phoneNumber = referenceBo.getPhoneNumber();
		this.priority = referenceBo.getPriority();
		this.statusId = EReferenceStatus.ACTIVE.getId();
    }

}
