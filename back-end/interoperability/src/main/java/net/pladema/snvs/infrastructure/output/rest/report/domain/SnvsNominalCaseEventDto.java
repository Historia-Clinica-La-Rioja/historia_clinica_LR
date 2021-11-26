package net.pladema.snvs.infrastructure.output.rest.report.domain;

import lombok.Getter;

@Getter
public class SnvsNominalCaseEventDto {

    private final Integer idGrupoEvento;

    private final Integer idEvento;

    private final Integer idClasificacionManualCaso;

    private final String fechaPapel;

    private final String idEstablecimientoCarga;

    public SnvsNominalCaseEventDto(Integer idGrupoEvento, Integer idEvento, Integer idClasificacionManualCaso,
                                   String idEstablecimientoCarga, String fechaPapel) {
        this.idGrupoEvento = idGrupoEvento;
        this.idEvento = idEvento;
        this.idClasificacionManualCaso = idClasificacionManualCaso;
        this.fechaPapel = fechaPapel;
        this.idEstablecimientoCarga = idEstablecimientoCarga;
    }
}
