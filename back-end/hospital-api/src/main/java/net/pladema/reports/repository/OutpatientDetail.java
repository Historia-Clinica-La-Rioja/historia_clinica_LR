package net.pladema.reports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OutpatientDetail {

    private String province;

    private String department;

    private String sisaCode;

    private String institution;

    private String patientSurname;

    private String patientFirstName;

    private String identificationType;

    private String identificationNumber;

    private String birthDate;

    private String gender;

    private String address;

    private String phoneNumber;

    private String email;

    private String startDate;

    private Integer clinicalSpecialtyId;

    private String clinicalSpecialty;

    private Integer professionalId;

    private String professionalName;

    private String reasons;

    private String problems;

    private String weight;

    private String height;

    private String systolicBloodPressure;

    private String diastolicBloodPressure;

}
