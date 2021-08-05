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
public class FormVVo {

    private String establishment;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

    private String patientGender;

    private LocalDate patientBirthDate;

    private String documentType;

    private String documentNumber;

    private String medicalCoverage;

    private String affiliateNumber;

    private String streetName;

    private String streetNumber;

    @JsonIgnore
    public Short getAge(){
        if (patientBirthDate == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(patientBirthDate, today);
        return (short) p.getYears();
    }
}
