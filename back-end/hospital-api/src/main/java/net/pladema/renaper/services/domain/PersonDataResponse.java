package net.pladema.renaper.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDataResponse {
	private String apellido;
	private String nombres;
	private String fechaNacimiento;
	private String cuil;
	private String calle;
	private String numero;
	private String piso;
	private String departamento;
	private String cpostal;
	private String barrio;
	private String monoblock;
	private String ciudad;
	private String municipio;
	private String provincia;
	private String pais;
	private String mensaf;
	private String origenf;
	private String fechaf;
	private String foto;
	private String sexo;
	private String numeroDocumento;
	private String fechaConsulta;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public PersonDataResponse(@JsonProperty("apellido") String apellido, @JsonProperty("nombres") String nombres,
			@JsonProperty("fechaNacimiento") String fechaNacimiento, @JsonProperty("cuil") String cuil,
			@JsonProperty("calle") String calle, @JsonProperty("numero") String numero, @JsonProperty("piso") String piso,
			@JsonProperty("departamento") String departamento, @JsonProperty("cpostal") String cpostal,
			@JsonProperty("barrio") String barrio, @JsonProperty("monoblock") String monoblock,
			@JsonProperty("ciudad") String ciudad, @JsonProperty("municipio") String municipio,
			@JsonProperty("provincia") String provincia, @JsonProperty("pais") String pais,
			@JsonProperty("mensaf") String mensaf, @JsonProperty("origenf") String origenf,
			@JsonProperty("fechaf") String fechaf, @JsonProperty("foto") String foto, @JsonProperty("sexo") String sexo,
			@JsonProperty("numeroDocumento") String numeroDocumento, @JsonProperty("fechaConsulta") String fechaConsulta) {
		this.apellido = apellido;
		this.nombres = nombres;
		this.fechaNacimiento = fechaNacimiento;
		this.cuil = cuil;
		this.calle = calle;
		this.numero = numero;
		this.piso = piso;
		this.departamento = departamento;
		this.cpostal = cpostal;
		this.barrio = barrio;
		this.monoblock = monoblock;
		this.ciudad = ciudad;
		this.municipio = municipio;
		this.provincia = provincia;
		this.pais = pais;
		this.mensaf = mensaf;
		this.origenf = origenf;
		this.fechaf = fechaf;
		this.foto = foto;
		this.sexo = sexo;
		this.numeroDocumento = numeroDocumento;
		this.fechaConsulta = fechaConsulta;
	}

	@Override
	public String toString() {
		return "PersonaResponse [" + (apellido != null ? "apellido=" + apellido + ", " : "")
				+ (nombres != null ? "nombres=" + nombres + ", " : "")
				+ (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "")
				+ (cuil != null ? "cuil=" + cuil + ", " : "") + (calle != null ? "calle=" + calle + ", " : "")
				+ (numero != null ? "numero=" + numero + ", " : "") + (piso != null ? "piso=" + piso + ", " : "")
				+ (departamento != null ? "departamento=" + departamento + ", " : "")
				+ (cpostal != null ? "cpostal=" + cpostal + ", " : "")
				+ (barrio != null ? "barrio=" + barrio + ", " : "")
				+ (monoblock != null ? "monoblock=" + monoblock + ", " : "")
				+ (ciudad != null ? "ciudad=" + ciudad + ", " : "")
				+ (municipio != null ? "municipio=" + municipio + ", " : "")
				+ (provincia != null ? "provincia=" + provincia + ", " : "")
				+ (pais != null ? "pais=" + pais + ", " : "") + (mensaf != null ? "mensaf=" + mensaf + ", " : "")
				+ (origenf != null ? "origenf=" + origenf + ", " : "")
				+ (fechaf != null ? "fechaf=" + fechaf + ", " : "") + (foto != null ? "foto=" + foto + ", " : "")
				+ (sexo != null ? "sexo=" + sexo + ", " : "")
				+ (numeroDocumento != null ? "numeroDocumento=" + numeroDocumento + ", " : "")
				+ (fechaConsulta != null ? "fechaConsulta=" + fechaConsulta : "") + "]";
	}

}
