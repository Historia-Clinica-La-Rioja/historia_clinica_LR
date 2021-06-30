package net.pladema.reports.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class InformeMensualOC {

    private String provincia;

    private String municipio;

    private String codEstable;

    private String institucion;

    private String apellidosPaciente;

    private String nombresPaciente;

    private String tipoDocumento;

    private String numeroDocumento;

    private String fechaNacimiento;

    private String genero;

    private String domicilio;

    private String numeroTelefono;

    private String email;

    private String fechaInicio;

    private String especialidad;

    private String apellidosProfesional;

    private String razonesConsulta;

    private String problemas;

    private String peso;

    private String talla;

    private String tensionSistolica;

    private String tensionDiastolica;

}
