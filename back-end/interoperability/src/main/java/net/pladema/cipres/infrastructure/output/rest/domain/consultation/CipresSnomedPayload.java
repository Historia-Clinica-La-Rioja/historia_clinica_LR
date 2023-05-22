package net.pladema.cipres.infrastructure.output.rest.domain.consultation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class CipresSnomedPayload {

	private String nombre;

	private String sctId;
}
