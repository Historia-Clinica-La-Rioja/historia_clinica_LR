package net.pladema.renaper.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonMedicalCoverageResponse {

	private String rnos;
	private String cobertura;
	private String servicio;
	private String fechaConsulta;
	
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public PersonMedicalCoverageResponse(@JsonProperty("rnos") String rnos, @JsonProperty("cobertura") String cobertura,@JsonProperty("servicio")  String servicio, @JsonProperty("fechaConsulta") String fechaConsulta) {
		this.rnos = rnos;
		this.cobertura = cobertura;
		this.servicio = servicio;
		this.fechaConsulta = fechaConsulta;
	}

	@Override
	public String toString() {
		return "MedicalCoveragePersonResponse [" + (rnos != null ? "rnos=" + rnos + ", " : "")
				+ (cobertura != null ? "cobertura=" + cobertura + ", " : "")
				+ (servicio != null ? "servicio=" + servicio + ", " : "")
				+ (fechaConsulta != null ? "fechaConsulta=" + fechaConsulta : "") + "]";
	}
	
}
