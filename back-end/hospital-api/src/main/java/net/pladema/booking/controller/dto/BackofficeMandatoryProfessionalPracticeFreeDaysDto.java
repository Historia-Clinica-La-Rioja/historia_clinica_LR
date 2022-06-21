package net.pladema.booking.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BackofficeMandatoryProfessionalPracticeFreeDaysDto {
    Integer id;
    Integer healthcareProfessionalId;
    Integer clinicalSpecialtyId;
    Integer mandatoryMedicalPracticeId;
    List<Short> days;
}
