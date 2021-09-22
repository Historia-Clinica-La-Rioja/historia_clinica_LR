package net.pladema.reports.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.FormVAppointmentVo;
import net.pladema.reports.repository.entity.FormVOutpatientVo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class FormVBo {

    private String establishment;

    private String completePatientName;

    private String address;

    private LocalDate reportDate;

    private String patientGender;

    private Short patientAge;

    private String documentType;

    private String documentNumber;

    private String medicalCoverage;

    private String affiliateNumber;

    private LocalDate consultationDate;

    private String problems;

    private String sisaCode;

    private String cie10Codes;

    public FormVBo(FormVOutpatientVo formVOutpatientVo){
        this.establishment = formVOutpatientVo.getEstablishment();
        this.completePatientName = Stream.of(formVOutpatientVo.getFirstName(), formVOutpatientVo.getMiddleNames(), formVOutpatientVo.getLastName(), formVOutpatientVo.getOtherLastNames())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.reportDate = LocalDate.now();
        this.patientGender = formVOutpatientVo.getPatientGender();
        this.patientAge = formVOutpatientVo.getAge();
        this.documentType = formVOutpatientVo.getDocumentType();
        this.documentNumber = formVOutpatientVo.getDocumentNumber();
        this.address = Stream.of(formVOutpatientVo.getStreetName(), formVOutpatientVo.getStreetNumber(), formVOutpatientVo.getCity())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.consultationDate = formVOutpatientVo.getConsultationDate();
        this.problems = formVOutpatientVo.getProblems();
        this.sisaCode = formVOutpatientVo.getSisaCode();
        this.cie10Codes = formVOutpatientVo.getCie10Codes();
    }

    public FormVBo(FormVAppointmentVo formVAppointmentVo){
        this.establishment = formVAppointmentVo.getEstablishment();
        this.completePatientName = Stream.of(formVAppointmentVo.getFirstName(), formVAppointmentVo.getMiddleNames(), formVAppointmentVo.getLastName(), formVAppointmentVo.getOtherLastNames())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.reportDate = LocalDate.now();
        this.patientGender = formVAppointmentVo.getPatientGender();
        this.patientAge = formVAppointmentVo.getAge();
        this.documentType = formVAppointmentVo.getDocumentType();
        this.documentNumber = formVAppointmentVo.getDocumentNumber();
        this.medicalCoverage = formVAppointmentVo.getMedicalCoverage();
        this.affiliateNumber = formVAppointmentVo.getAffiliateNumber();
        this.address = Stream.of(formVAppointmentVo.getStreetName(), formVAppointmentVo.getStreetNumber(), formVAppointmentVo.getCity())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.sisaCode = formVAppointmentVo.getSisaCode();
    }
}
