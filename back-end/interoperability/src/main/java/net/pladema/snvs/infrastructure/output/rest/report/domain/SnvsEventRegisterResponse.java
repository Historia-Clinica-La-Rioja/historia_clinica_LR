package net.pladema.snvs.infrastructure.output.rest.report.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@JsonDeserialize(using = SnvsEventRegisterResponseDeserializer.class)
public class SnvsEventRegisterResponse {

	@ToString.Include
	private String errors;

	@ToString.Include
	private String status;

	@ToString.Include
	private String timestamp;

	@ToString.Include
	private String resultado;

	@ToString.Include
	private Integer idCaso;

	@ToString.Include
	private String description;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public SnvsEventRegisterResponse (@JsonProperty("errors") String errors,
									@JsonProperty("status") String status,
									@JsonProperty("timestamp") String timestamp,
									@JsonProperty("resultado") String resultado,
									@JsonProperty("id_caso") Integer idCaso,
									@JsonProperty("description") String description){
		this.errors = errors;
		this.status = status;
		this.timestamp = timestamp;
		this.resultado = resultado;
		this.idCaso = idCaso;
		this.description = description;
	}
}
