package net.pladema.cipres.infrastructure.output.rest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class CipresPatientPayload {

	private String nombre;

	private String apellido;

	private String tipoDocumento;

	private String numeroDocumento;

	private String sexo;

	private String fechaNacimiento;

}
