package net.pladema.clinichistory.outpatient.repository.domain;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "outpatient_consultation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OutpatientConsultation extends SGXAuditableEntity<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 2944833030892268840L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "clinical_specialty_id")
    private Integer clinicalSpecialtyId;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "billable", nullable = false)
    private Boolean billable;

    @Column(name = "patient_medical_coverage_id")
    private Integer patientMedicalCoverageId;

    public OutpatientConsultation(Integer institutionId, Integer patientId, Integer doctorId, boolean billable,
                                  Integer clinicalSpecialtyId, Integer patientMedicalCoverageId) {
        super();
        this.institutionId = institutionId;
        this.patientId = patientId;
        this.billable = billable;
        this.startDate = LocalDate.now();
        this.doctorId = doctorId;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
        this.patientMedicalCoverageId = patientMedicalCoverageId;
    }
}