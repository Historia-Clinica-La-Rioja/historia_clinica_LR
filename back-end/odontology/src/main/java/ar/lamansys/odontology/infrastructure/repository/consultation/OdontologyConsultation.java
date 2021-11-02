package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ConsultationInfoBo;
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
@Table(name = "odontology_consultation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OdontologyConsultation extends SGXAuditableEntity<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "clinical_specialty_id")
    private Integer clinicalSpecialtyId;

    @Column(name = "institution_id")
    private Integer institutionId;

    @Column(name = "patient_medical_coverage_id")
    private Integer patientMedicalCoverageId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "performed_date")
    private LocalDate performedDate;

    @Column(name = "billable", nullable = false)
    private Boolean billable;

    public OdontologyConsultation(Integer id, Integer institutionId, Integer patientId, Integer doctorId,
                               Integer clinicalSpecialtyId, LocalDate performedDate, boolean billable) {
        super();
        this.id = id;
        this.institutionId = institutionId;
        this.patientId = patientId;
        this.billable = billable;
        this.doctorId = doctorId;
        this.performedDate = performedDate;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
    }

    public OdontologyConsultation(ConsultationInfoBo consultation) {
        super();
        this.id = consultation.getId();
        this.institutionId = consultation.getInstitutionId();
        this.patientId = consultation.getPatientId();
        this.billable = consultation.isBillable();
        this.doctorId = consultation.getDoctorId();
        this.performedDate = consultation.getPerformedDate();
        this.clinicalSpecialtyId = consultation.getClinicalSpecialtyId();
        this.patientMedicalCoverageId = consultation.getPatientMedicalCoverageId();
    }

}
