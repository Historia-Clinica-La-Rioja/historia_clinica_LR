package net.pladema.cipres.infrastructure.output.rest.domain.patient;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CipresPatientAddressPayload {

	private String nacionalidad;

	private String localidad;

	private String telefono;

	private String celular;

	private String email;

	private String calle;

	private String nro;

	private Integer piso;

	private String departamento;

	private String barrio;

	private String cp;

	private String paciente;

}
