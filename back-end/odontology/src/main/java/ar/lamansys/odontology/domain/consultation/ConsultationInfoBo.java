package ar.lamansys.odontology.domain.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class ConsultationInfoBo {

    private Integer id;

    private Integer patientId;

    private Integer patientMedicalCoverageId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private LocalDate performedDate;

    private Integer doctorId;

    private boolean billable;

    List<ConsultationReasonBo> reasons;

	private Integer hierarchicalUnitId;

    public static ConsultationInfoBo newConsultationInfoBo(ConsultationBo consultation, Integer doctorId, LocalDate performedDate, boolean billable) {
		return new ConsultationInfoBo(null, consultation.getPatientId(), consultation.getPatientMedicalCoverageId(), consultation.getInstitutionId(), consultation.getClinicalSpecialtyId(), performedDate, doctorId, billable, consultation.getReasons(), consultation.getHierarchicalUnitId());
	}
	
    public ConsultationInfoBo(Integer id, ConsultationBo consultation, Integer doctorId, LocalDate performedDate, boolean billable) {
        this.id = id;
        this.patientId = consultation.getPatientId();
        this.institutionId = consultation.getInstitutionId();
        this.clinicalSpecialtyId = consultation.getClinicalSpecialtyId();
        this.reasons = consultation.getReasons();
        this.patientMedicalCoverageId = consultation.getPatientMedicalCoverageId();
        this.doctorId = doctorId;
        this.performedDate = performedDate;
        this.billable = billable;
    }

}
