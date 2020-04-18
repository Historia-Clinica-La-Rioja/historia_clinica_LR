package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InternmentPatientDto {

    private Integer patientId;

    private String name;

    private String surname;

    private BedDto bed;

    private ResponsableDoctorDto doctor;






}
