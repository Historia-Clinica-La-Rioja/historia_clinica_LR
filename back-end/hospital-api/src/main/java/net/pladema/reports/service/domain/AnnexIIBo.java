package net.pladema.reports.service.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;

@Getter
@Setter
public class AnnexIIBo {

    private LocalDate reportDate;

    private String establishment;

    private String completePatientName;

    private String documentType;

    private String documentNumber;

    private String patientGender;

    private Short patientAge;

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;

    private String affiliateNumber;

    private Boolean existsConsultation;

    private Boolean hasProcedures;

    private String specialty;

    private LocalDate consultationDate;

    private String sisaCode;

    private String problems;

	private Integer rnos;

    public AnnexIIBo(AnnexIIOutpatientVo annexIIOutpatientVo){
        this.reportDate = LocalDate.now();
        this.establishment = annexIIOutpatientVo.getEstablishment();
        this.completePatientName = annexIIOutpatientVo.getCompletePatientName();
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
    }

    public AnnexIIBo(AnnexIIAppointmentVo annexIIAppointmentVo){
        this.reportDate = LocalDate.now();
        this.establishment = annexIIAppointmentVo.getEstablishment();
        this.completePatientName = annexIIAppointmentVo.getCompletePatientName();
        this.documentType = annexIIAppointmentVo.getDocumentType ();
        this.documentNumber = annexIIAppointmentVo.getDocumentNumber();
        this.patientGender = annexIIAppointmentVo.getPatientGender();
        this.patientAge = annexIIAppointmentVo.getAge();
        this.sisaCode = annexIIAppointmentVo.getSisaCode();

        this.attentionDate = annexIIAppointmentVo.getAttentionDate();
        this.appointmentState = annexIIAppointmentVo.getAppointmentState();
        this.medicalCoverage = annexIIAppointmentVo.getMedicalCoverage();
        this.affiliateNumber = annexIIAppointmentVo.getAffiliateNumber();
		this.rnos = annexIIAppointmentVo.getRnos();
    }

}
