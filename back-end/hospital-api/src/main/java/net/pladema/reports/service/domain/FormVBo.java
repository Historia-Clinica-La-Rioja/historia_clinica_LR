package net.pladema.reports.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.FormVVo;

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

    public FormVBo(FormVVo formVVo) {
        this.establishment = formVVo.getEstablishment();
        this.completePatientName = Stream.of(formVVo.getFirstName(), formVVo.getMiddleNames(), formVVo.getLastName(), formVVo.getOtherLastNames())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.reportDate = LocalDate.now();
        this.patientGender = formVVo.getPatientGender();
        this.patientAge = formVVo.getAge();
        this.documentType = formVVo.getDocumentType();
        this.documentNumber = formVVo.getDocumentNumber();
        this.medicalCoverage = formVVo.getMedicalCoverage();
        this.affiliateNumber = formVVo.getAffiliateNumber();
        this.address = Stream.of(formVVo.getStreetName(), formVVo.getStreetNumber())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }
}
