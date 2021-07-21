package net.pladema.reports.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.AnnexIIVo;

import java.time.LocalDate;

@Getter
@Setter
public class AnnexIIBo {

    private LocalDate fechaReporte;

    private String hospital;

    private String nombreCompletoPaciente;

    private String tipoDocumento;

    private String numeroDocumento;

    private String sexoPaciente;

    private Short edadPaciente;

    //private Boolean tienePractica;

    private LocalDate fechaAtencion;

    public AnnexIIBo(AnnexIIVo annexIIVo){
        this.fechaReporte = LocalDate.now();
        this.hospital = annexIIVo.getHospital();
        this.nombreCompletoPaciente = annexIIVo.getNombreCompletoPaciente();
        this.tipoDocumento = annexIIVo.getTipoDocumento ();
        this.numeroDocumento = annexIIVo.getNumeroDocumento();
        this.sexoPaciente = annexIIVo.getSexoPaciente();
        this.edadPaciente = annexIIVo.getAge();
        this.fechaAtencion = annexIIVo.getFechaAtencion();
    }
}
