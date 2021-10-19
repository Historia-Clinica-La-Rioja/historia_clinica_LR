package net.pladema.reports.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class FormVOutpatientVo extends FormVVo{

    private LocalDate consultationDate;

    private String problems;

    private String cie10Codes;

    public FormVOutpatientVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender,
                             LocalDate patientBirthDate, String documentType, String documentNumber, LocalDate consultationDate, String problems,
                             String streetName, String streetNumber, String city, String sisaCode, String cie10Codes){
        super(establishment, firstName, middleNames, lastName, otherLastNames, patientGender, patientBirthDate, documentType,
                documentNumber, streetName, streetNumber, city, sisaCode);
        this.consultationDate = consultationDate;
        this.problems = problems;
        this.cie10Codes = cie10Codes;
    }

    @JsonIgnore
    public Short getAge(){
        if (super.getPatientBirthDate() == null)
            return null;
        Period p = Period.between(super.getPatientBirthDate(), consultationDate);
        return (short) p.getYears();
    }

}
