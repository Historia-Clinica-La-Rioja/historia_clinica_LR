package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

    private String streetName;

    private String streetNumber;

    private String city;

    private String sisaCode;

    public FormVVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender,
                   LocalDate patientBirthDate, String documentType, String documentNumber, String streetName, String streetNumber, String city, String sisaCode){
        this.establishment = establishment;
        this.firstName = firstName;
        this.middleNames = middleNames;
        this.lastName = lastName;
        this.otherLastNames = otherLastNames;
        this.patientGender = patientGender;
        this.patientBirthDate = patientBirthDate;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
        this.sisaCode = sisaCode;
    }
}
