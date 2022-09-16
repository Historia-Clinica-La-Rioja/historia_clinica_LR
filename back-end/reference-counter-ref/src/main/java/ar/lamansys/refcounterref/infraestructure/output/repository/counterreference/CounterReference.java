package ar.lamansys.refcounterref.infraestructure.output.repository.counterreference;

import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "counter_reference")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CounterReference extends SGXAuditableEntity<Integer> {

    private static final long serialVersionUID = -1773334233115732184L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reference_id", nullable = false)
    private Integer referenceId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "clinical_specialty_id", nullable = false)
    private Integer clinicalSpecialtyId;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "patient_medical_coverage_id")
    private Integer patientMedicalCoverageId;

    @Column(name = "billable", nullable = false)
    private Boolean billable;

    @Column(name = "performed_date", nullable = false)
    private LocalDate performedDate;

	@Column(name = "closure_type_id", nullable = false)
	private Short closureTypeId;

	public CounterReference(CounterReferenceInfoBo counterReferenceInfoBo) {
        super();
        this.id = counterReferenceInfoBo.getId();
        this.referenceId = counterReferenceInfoBo.getReferenceId();
        this.institutionId = counterReferenceInfoBo.getInstitutionId();
        this.patientId = counterReferenceInfoBo.getPatientId();
        this.patientMedicalCoverageId = counterReferenceInfoBo.getPatientMedicalCoverageId();
        this.billable = counterReferenceInfoBo.isBillable();
        this.doctorId = counterReferenceInfoBo.getDoctorId();
        this.clinicalSpecialtyId = counterReferenceInfoBo.getClinicalSpecialtyId();
        this.performedDate = counterReferenceInfoBo.getPerformedDate();
		this.closureTypeId = counterReferenceInfoBo.getClosureTypeId();
    }

}