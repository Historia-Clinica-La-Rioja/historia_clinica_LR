package net.pladema.reports.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnexIIVo {

    private String establishment;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

    private String documentType;

    private String documentNumber;

    private String patientGender;

    private LocalDate patientBirthDate;

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;

    private String affiliateNumber;

    @JsonIgnore
    public Short getAge(){
        if (patientBirthDate == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(patientBirthDate, today);
        return (short) p.getYears();
    }
}
