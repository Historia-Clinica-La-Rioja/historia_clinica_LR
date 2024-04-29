package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EntityListeners(SGXAuditListener.class)
@Table(name = "anesthetic_report")
@Entity
public class AnestheticReport extends SGXAuditableEntity<Integer> implements SGXDocumentEntity {

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

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "billable", nullable = false)
    private Boolean billable;

    @Column(name = "patient_medical_coverage_id")
    private Integer patientMedicalCoverageId;

    @Column(name = "anesthetic_chart")
    private String anestheticChart;

}
