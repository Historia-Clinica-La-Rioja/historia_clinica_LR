package net.pladema.cipres.infrastructure.output.rest.domain.consultation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CipresDatosClinicosPayload {

	private String peso;

	private String talla;

	private Integer perimetroCefalico;

	private String tensionArterial;

	private Float imc;

}
