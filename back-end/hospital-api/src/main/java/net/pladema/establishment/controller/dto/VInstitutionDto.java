package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class VInstitutionDto {

    private Double latitude;

    private Double longitude;

    //#cantidad adultos mayores internados
    private long patientCount = 0L;

    //#cantidad adultos mayores con factores de riesgo las últimas 24hh
    private long patientWithRiskFactorCount = 0L;

    //#fecha mas lejana de última carga de factor de riesgo
    private LocalDateTime lastDateRiskFactor;

    //#cantidad adultos mayores con marca sospechoso (covid)
    private long patientWithCovidPresumtiveCount = 0L;
}
