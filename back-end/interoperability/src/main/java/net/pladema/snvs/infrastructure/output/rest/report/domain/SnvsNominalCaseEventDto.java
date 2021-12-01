package net.pladema.snvs.infrastructure.output.rest.report.domain;

import lombok.Getter;

@Getter
public class SnvsNominalCaseEventDto {

    private final Integer idEvento;

    private final Integer idGrupoEvento;

    private final Integer idClasificacionManualCaso;

    private final String fechaPapel;

    private final String idEstablecimientoCarga;

    public SnvsNominalCaseEventDto(Integer idEvento, Integer idGrupoEvento, Integer idClasificacionManualCaso,
                                   String idEstablecimientoCarga, String fechaPapel) {
        this.idEvento = idEvento;
        this.idGrupoEvento = idGrupoEvento;
        this.idClasificacionManualCaso = idClasificacionManualCaso;
        this.fechaPapel = fechaPapel;
        this.idEstablecimientoCarga = idEstablecimientoCarga;
    }
}
