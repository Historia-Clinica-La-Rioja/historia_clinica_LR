package net.pladema.booking.controller.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BackofficeMandatoryMedicalPracticeDto {
    Integer id;
    String description;
    String mmpCode;
    Integer snomedId;
}
