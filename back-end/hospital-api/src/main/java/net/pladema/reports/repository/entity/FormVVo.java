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

    private String city;

    private LocalDate consultationDate;

    private String problems;

    public FormVVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender,
                   LocalDate patientBirthDate, String documentType, String documentNumber, String medicalCoverage, String affiliateNumber,
                   String streetName, String streetNumber,String city) {
        this.establishment = establishment;
        this.firstName = firstName;
        this.middleNames = middleNames;
        this.lastName = lastName;
        this.otherLastNames = otherLastNames;
        this.patientGender = patientGender;
        this.patientBirthDate = patientBirthDate;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.medicalCoverage = medicalCoverage;
        this.affiliateNumber = affiliateNumber;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
    }

    public FormVVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender,
                   LocalDate patientBirthDate, String documentType, String documentNumber, LocalDate consultationDate, String problems,
                   String streetName, String streetNumber,String city) {
        this.establishment = establishment;
        this.firstName = firstName;
        this.middleNames = middleNames;
        this.lastName = lastName;
        this.otherLastNames = otherLastNames;
        this.patientGender = patientGender;
        this.patientBirthDate = patientBirthDate;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.consultationDate = consultationDate;
        this.problems = problems;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
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
