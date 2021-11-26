package net.pladema.snvs.infrastructure.output.rest.report.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SnvsAddressDto {

    private String calle;

    private Integer idDepartamento;

    private Integer idLocalidad;

    private Short idProvincia;

    private Integer idPais;

}
