package net.pladema.reports.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.reports.repository.entity.AnnexIIVo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private String estadoTurno;

    private LocalDate fechaAtencion;

    private String obraSocial;

    private String numeroAfiliado;

    public AnnexIIBo(AnnexIIVo annexIIVo){
        this.fechaReporte = LocalDate.now();
        this.hospital = annexIIVo.getHospital();
        this.nombreCompletoPaciente = Stream.of(annexIIVo.getFirstName(), annexIIVo.getMiddleNames(), annexIIVo.getLastName(),annexIIVo.getOtherLastNames())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        this.tipoDocumento = annexIIVo.getTipoDocumento ();
        this.numeroDocumento = annexIIVo.getNumeroDocumento();
        this.sexoPaciente = annexIIVo.getSexoPaciente();
        this.edadPaciente = annexIIVo.getAge();
        this.fechaAtencion = annexIIVo.getFechaAtencion();
        this.estadoTurno = annexIIVo.getEstadoTurno();
        this.obraSocial = annexIIVo.getObraSocial();
        this.numeroAfiliado = annexIIVo.getNumeroAfiliado();
    }
}
