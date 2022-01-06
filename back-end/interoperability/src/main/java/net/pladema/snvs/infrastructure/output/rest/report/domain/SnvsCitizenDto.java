package net.pladema.snvs.infrastructure.output.rest.report.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SnvsCitizenDto {

    private String apellido;

    private String nombre;

    private Short tipoDocumento;

    private String numeroDocumento;

    private String sexo;

    private String fechaNacimiento;

    private Short paisEmisionTipoDocumento;

    private String seDeclaraPuebloIndigena;

    private SnvsAddressDto domicilio;

    private String telefono;

    private String mail;

    private SnvsTutorDto personaACargo;

}
