package net.pladema.reports.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;

@Getter
@Setter
public class AnnexIIBo {

    private String establishment;

    private String completePatientName;

	private String formalPatientName;

    private String documentType;

    private String documentNumber;

    private String patientGender;

    private Short patientAge;

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;
	private String medicalCoverageCuit;

    private String affiliateNumber;

    private Boolean existsConsultation;

    private Boolean hasProcedures;

    private String specialty;

    private LocalDate consultationDate;

    private String sisaCode;

    private String problems;

	private Integer rnos;

	private LocalDate medicalCoverageStartDate;

	private LocalDate medicalCoverageEndDate;

	private List<AnnexIIProcedureBo> procedures;

	private LocalDateTime proceduresIngressDate;

	private LocalDateTime proceduresEgressDate;

	private Float proceduresTotal;

	private Boolean showProcedures;
	private Integer missingProcedures;

	private Short patientIdentityAccreditationStatusId;

	private AnnexIIProfessionalBo professional;

    public AnnexIIBo(AnnexIIOutpatientVo annexIIOutpatientVo){
        this.establishment = annexIIOutpatientVo.getEstablishment();
        this.completePatientName = annexIIOutpatientVo.getCompletePatientName();
		this.formalPatientName = annexIIOutpatientVo.getFormalPatientName();
        this.documentType = annexIIOutpatientVo.getDocumentType ();
        this.documentNumber = annexIIOutpatientVo.getDocumentNumber();
        this.patientGender = annexIIOutpatientVo.getPatientGender();
        this.patientAge = annexIIOutpatientVo.getAge();
        this.sisaCode = annexIIOutpatientVo.getSisaCode();

        this.existsConsultation = annexIIOutpatientVo.getExistsConsultation();
        this.hasProcedures = annexIIOutpatientVo.getHasProcedures();
        this.specialty = annexIIOutpatientVo.getSpecialty();
        this.consultationDate = annexIIOutpatientVo.getConsultationDate();
        this.problems = annexIIOutpatientVo.getProblems();

		this.medicalCoverage = annexIIOutpatientVo.getMedicalCoverage();
		this.medicalCoverageCuit = annexIIOutpatientVo.getMedicalCoverageCuit();
		this.rnos = annexIIOutpatientVo.getRnos();
    }

    public AnnexIIBo(AnnexIIAppointmentVo annexIIAppointmentVo){
        this.establishment = annexIIAppointmentVo.getEstablishment();
        this.completePatientName = annexIIAppointmentVo.getCompletePatientName();
		this.formalPatientName = annexIIAppointmentVo.getFormalPatientName();
        this.documentType = annexIIAppointmentVo.getDocumentType ();
        this.documentNumber = annexIIAppointmentVo.getDocumentNumber();
        this.patientGender = annexIIAppointmentVo.getPatientGender();
        this.patientAge = annexIIAppointmentVo.getAge();
        this.sisaCode = annexIIAppointmentVo.getSisaCode();

        this.attentionDate = annexIIAppointmentVo.getAttentionDate();
        this.appointmentState = annexIIAppointmentVo.getAppointmentState();
        this.medicalCoverage = annexIIAppointmentVo.getMedicalCoverage();
        this.medicalCoverageCuit = annexIIAppointmentVo.getMedicalCoverageCuit();
		this.rnos = annexIIAppointmentVo.getRnos();
		this.patientIdentityAccreditationStatusId = annexIIAppointmentVo.getPatientIdentityAccreditationStatusId();
    }

    public LocalDate getConsultationOrAttentionDate(){
    	if (this.getConsultationDate() != null)
    		return this.getConsultationDate();
    	else
    		return this.getAttentionDate();
    }
}
