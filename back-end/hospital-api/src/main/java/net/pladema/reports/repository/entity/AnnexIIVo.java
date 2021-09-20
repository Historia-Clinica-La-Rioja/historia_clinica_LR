package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class AnnexIIVo {

    private String establishment;

    private String completePatientName;

    private String patientGender;

    private LocalDate patientBirthDate;

    private String documentType;

    private String documentNumber;

    private String sisaCode;

    public AnnexIIVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender,
                     LocalDate patientBirthDate, String documentType, String documentNumber, String sisaCode){
        this.establishment = establishment;
        this.completePatientName = Stream.of(firstName, middleNames, lastName, otherLastNames)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.patientGender = patientGender;
        this.patientBirthDate = patientBirthDate;
        this.sisaCode = sisaCode;
    }
}
