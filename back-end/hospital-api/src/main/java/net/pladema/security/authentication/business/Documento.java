package net.pladema.security.authentication.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Documento {
	private String numeroDocumento;
	private TipoDocumento tipoDocumento;
}
