package net.pladema.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BackofficeClinicalSpecialtyMandatoryMedicalPracticeDto {
    Integer id;
    Integer clinicalSpecialtyId;
    Integer mandatoryMedicalPracticeId;
    String practiceRecommendations;
}

