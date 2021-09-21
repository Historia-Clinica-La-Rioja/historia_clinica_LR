package net.pladema.reports.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class FormVAppointmentVo extends FormVVo{

    private String medicalCoverage;

    private String affiliateNumber;

    public FormVAppointmentVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender,
                              LocalDate patientBirthDate, String documentType, String documentNumber, String streetName,
                              String streetNumber, String city, String medicalCoverage, String affiliateNumber, String sisaCode){
        super(establishment, firstName, middleNames, lastName, otherLastNames, patientGender, patientBirthDate, documentType,
                documentNumber, streetName, streetNumber, city, sisaCode);
        this.medicalCoverage = medicalCoverage;
        this.affiliateNumber = affiliateNumber;
    }

    @JsonIgnore
    public Short getAge(){
        if (super.getPatientBirthDate() == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(super.getPatientBirthDate(), today);
        return (short) p.getYears();
    }
}
