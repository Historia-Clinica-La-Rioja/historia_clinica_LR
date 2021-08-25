package net.pladema.reports.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.AnnexIIVo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public AnnexIIBo(AnnexIIVo annexIIVo){
        this.reportDate = LocalDate.now();
        this.establishment = annexIIVo.getEstablishment();
        this.completePatientName = annexIIVo.getCompletePatientName();
        this.documentType = annexIIVo.getDocumentType ();
        this.documentNumber = annexIIVo.getDocumentNumber();
        this.patientGender = annexIIVo.getPatientGender();
        this.patientAge = annexIIVo.getAge();
        this.attentionDate = annexIIVo.getAttentionDate();
        this.appointmentState = annexIIVo.getAppointmentState();
        this.medicalCoverage = annexIIVo.getMedicalCoverage();
        this.affiliateNumber = annexIIVo.getAffiliateNumber();
        this.existsConsultation = annexIIVo.getExistsConsultation();
        this.hasProcedures = annexIIVo.getHasProcedures();
        this.specialty = annexIIVo.getSpecialty();
        this.consultationDate = annexIIVo.getConsultationDate();
    }
}
