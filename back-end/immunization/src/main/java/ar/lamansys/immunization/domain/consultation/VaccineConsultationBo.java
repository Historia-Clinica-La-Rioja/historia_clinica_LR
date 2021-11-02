package ar.lamansys.immunization.domain.consultation;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class VaccineConsultationBo {

    private Integer id;

    private Integer patientId;

    private Integer patientMedicalCoverageId;

    private Integer clinicalSpecialtyId;

    private Integer institutionId;

    private Integer doctorId;

    private LocalDate performedDate;

    private boolean billable;

    public VaccineConsultationBo(Integer id, Integer patientId,
                                 Integer patientMedicalCoverageId,
                                 Integer clinicalSpecialtyId,
                                 Integer institutionId,
                                 Integer doctorId,
                                 LocalDate performedDate,
                                 boolean billable) {
        this.id = id;
        this.patientId = patientId;
        this.patientMedicalCoverageId = patientMedicalCoverageId;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
        this.institutionId = institutionId;
        this.doctorId = doctorId;
        this.performedDate = performedDate;
        this.billable = billable;
    }
}
