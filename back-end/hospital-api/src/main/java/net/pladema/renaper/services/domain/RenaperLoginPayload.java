package net.pladema.renaper.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RenaperLoginPayload {

    @JsonProperty("nombre")
    public final String nombre;

    @JsonProperty("clave")
    public final String clave;

    @JsonProperty("codDominio")
    public final String codDominio;

	public RenaperLoginPayload(String nombre, String clave, String codDominio) {
		this.nombre = nombre;
		this.clave = clave;
		this.codDominio = codDominio;
	}

}
