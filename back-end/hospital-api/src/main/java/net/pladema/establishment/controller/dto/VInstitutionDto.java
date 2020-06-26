package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class VInstitutionDto {

    private Double latitud;

    private Double longitud;

    //#cantidad adultos mayores internados
    private long patientCount = 0L;

    //#cantidad adultos mayores con signos vitales las últimas 24hh
    private long patientWithVitalSignCount = 0L;

    //#fecha mas lejana de última carga de signo vital
    private LocalDateTime lastDateVitalSign;

    //#cantidad adultos mayores con marca sospechoso (covid)
    private long patientWithCovidPresumtiveCount = 0L;
}
