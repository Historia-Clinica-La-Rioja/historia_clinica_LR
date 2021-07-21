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

    private String nombreCompletoPaciente;

    private String tipoDocumento;

    private String numeroDocumento;

    private String sexoPaciente;

    private LocalDate fechaNacPaciente;

    //private Boolean tienePractica;

    private LocalDate fechaAtencion;

    @JsonIgnore
    public Short getAge(){
        if (fechaNacPaciente == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(fechaNacPaciente, today);
        return (short) p.getYears();
    }
}
