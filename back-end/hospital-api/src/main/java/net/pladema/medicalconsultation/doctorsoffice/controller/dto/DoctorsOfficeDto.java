package net.pladema.medicalconsultation.doctorsoffice.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class DoctorsOfficeDto {

    private Integer id;

    private String description;

    private LocalTime openingTime;

    private LocalTime closingTime;
}
