package net.pladema.reports.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnnexIIDto {

    private LocalDate fechaReporte;

    private String hospital;

    private String nombreCompletoPaciente;

    private String tipoDocumento;

    private String numeroDocumento;

    private String sexoPaciente;

    private Short edadPaciente;

    private String estadoTurno;

    private LocalDate fechaAtencion;

    private String obraSocial;
}
