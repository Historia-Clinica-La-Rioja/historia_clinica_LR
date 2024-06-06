package ar.lamansys.nursing.domain;

import lombok.Getter;
import lombok.ToString;
import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Getter
@ToString
@AllArgsConstructor
public class NursingConsultationInfoBo {

    private Integer id;

    private Integer patientId;

    private Integer patientMedicalCoverageId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private LocalDate performedDate;

    private Integer doctorId;

    private boolean billable;

	private Integer hierarchicalUnitId;

    public static NursingConsultationInfoBo newNursingConsultationInfoBo(NursingConsultationBo nursingConsultation, Integer doctorId, LocalDate performedDate, boolean billable) {
		return new NursingConsultationInfoBo(null, nursingConsultation.getPatientId(), nursingConsultation.getPatientMedicalCoverageId(), nursingConsultation.getInstitutionId(), nursingConsultation.getClinicalSpecialtyId(), performedDate, doctorId, billable, nursingConsultation.getHierarchicalUnitId());
	}

    public NursingConsultationInfoBo(Integer id, NursingConsultationBo nursingConsultation, Integer doctorId, LocalDate performedDate, boolean billable) {
        this.id = id;
        this.patientId = nursingConsultation.getPatientId();
        this.institutionId = nursingConsultation.getInstitutionId();
        this.clinicalSpecialtyId = nursingConsultation.getClinicalSpecialtyId();
        this.patientMedicalCoverageId = nursingConsultation.getPatientMedicalCoverageId();
        this.doctorId = doctorId;
        this.performedDate = performedDate;
        this.billable = billable;
    }

}
