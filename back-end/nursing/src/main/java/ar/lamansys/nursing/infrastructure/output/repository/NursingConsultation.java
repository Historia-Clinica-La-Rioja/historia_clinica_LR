package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.nursing.domain.NursingConsultationInfoBo;
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
@Table(name = "nursing_consultation")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class NursingConsultation extends SGXAuditableEntity<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 4223110310327291883L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "patient_medical_coverage_id")
    private Integer patientMedicalCoverageId;

    @Column(name = "clinical_specialty_id")
    private Integer clinicalSpecialtyId;

    @Column(name = "institution_id")
    private Integer institutionId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "billable", nullable = false)
    private Boolean billable;

    @Column(name = "performed_date")
    private LocalDate performedDate;

    public NursingConsultation(NursingConsultationInfoBo nursingConsultationInfoBo) {
        super();
        this.id = nursingConsultationInfoBo.getId();
        this.institutionId = nursingConsultationInfoBo.getInstitutionId();
        this.patientId = nursingConsultationInfoBo.getPatientId();
        this.patientMedicalCoverageId = nursingConsultationInfoBo.getPatientMedicalCoverageId();
        this.billable = nursingConsultationInfoBo.isBillable();
        this.doctorId = nursingConsultationInfoBo.getDoctorId();
        this.clinicalSpecialtyId = nursingConsultationInfoBo.getClinicalSpecialtyId();
        this.performedDate = nursingConsultationInfoBo.getPerformedDate();
    }

}