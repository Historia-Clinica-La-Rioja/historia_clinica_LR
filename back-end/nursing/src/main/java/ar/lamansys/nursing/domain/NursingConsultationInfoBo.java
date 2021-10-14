package ar.lamansys.nursing.domain;

import ar.lamansys.nursing.domain.NursingConsultationBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class NursingConsultationInfoBo {

    private Integer id;

    private Integer patientId;

    private Integer patientMedicalCoverageId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private LocalDate performedDate;

    private Integer doctorId;

    private boolean billable;

    public NursingConsultationInfoBo(Integer id, NursingConsultationBo nursingConsultation, Integer patientMedicalCoverageId, Integer doctorId, LocalDate performedDate, boolean billable) {
        this.id = id;
        this.patientId = nursingConsultation.getPatientId();
        this.institutionId = nursingConsultation.getInstitutionId();
        this.clinicalSpecialtyId = nursingConsultation.getClinicalSpecialtyId();
        this.patientMedicalCoverageId = patientMedicalCoverageId;
        this.doctorId = doctorId;
        this.performedDate = performedDate;
        this.billable = billable;
    }

}
