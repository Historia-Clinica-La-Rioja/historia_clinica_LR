package net.pladema.reports.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnexIIVo {

    private String establishment;

    private String completePatientName;

    private String documentType;

    private String documentNumber;

    private String patientGender;

    private LocalDate patientBirthDate;

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;

    private String affiliateNumber;

    private Boolean existsConsultation;

    private Boolean hasProcedures;

    private String specialty;

    private LocalDate consultationDate;

    public AnnexIIVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String documentType, String documentNumber, String patientGender,
             LocalDate patientBirthDate, String appointmentState, LocalDate attentionDate, String medicalCoverage, String affiliateNumber){
        this.establishment = establishment;
        this.completePatientName = Stream.of(firstName, middleNames, lastName, otherLastNames)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.patientGender = patientGender;
        this.patientBirthDate = patientBirthDate;
        this.appointmentState = appointmentState;
        this.attentionDate = attentionDate;
        this.medicalCoverage = medicalCoverage;
        this.affiliateNumber = affiliateNumber;
    }

    public AnnexIIVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender, LocalDate patientBirthDate,
                     String documentType, String documentNumber, LocalDate consultationDate, Boolean hasProcedures, String specialty){
        this.establishment = establishment;
        this.completePatientName = Stream.of(firstName, middleNames, lastName, otherLastNames)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.patientGender = patientGender;
        this.patientBirthDate = patientBirthDate;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.consultationDate = consultationDate;
        this.hasProcedures = hasProcedures == null ? false : hasProcedures;
        this.existsConsultation = true;
        this.specialty = specialty;
    }

    @JsonIgnore
    public Short getAge(){
        if (patientBirthDate == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(patientBirthDate, today);
        return (short) p.getYears();
    }
}
