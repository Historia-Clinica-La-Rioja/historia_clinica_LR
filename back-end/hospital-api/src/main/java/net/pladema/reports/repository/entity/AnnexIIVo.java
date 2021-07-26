package net.pladema.reports.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnexIIVo {

    private String hospital;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

    private String tipoDocumento;

    private String numeroDocumento;

    private String sexoPaciente;

    private LocalDate fechaNacPaciente;

    private String estadoTurno;

    private LocalDate fechaAtencion;

    private String obraSocial;

    private String numeroAfiliado;

    @JsonIgnore
    public Short getAge(){
        if (fechaNacPaciente == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(fechaNacPaciente, today);
        return (short) p.getYears();
    }
}
