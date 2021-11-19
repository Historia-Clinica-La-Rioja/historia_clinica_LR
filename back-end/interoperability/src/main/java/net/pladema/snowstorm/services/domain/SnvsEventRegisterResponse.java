package net.pladema.snowstorm.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnvsEventRegisterResponse {

	private List<String> errors;
	private String status;
	private LocalDateTime timestamp;
	private String resultado;
	private Integer id_caso;
	private String description;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public SnvsEventRegisterResponse (@JsonProperty("errors") List<String> errors,
									@JsonProperty("status") String status,
									@JsonProperty("timestamp") LocalDateTime timestamp,
									@JsonProperty("resultado") String resultado,
									@JsonProperty("id_caso") Integer id_caso,
									@JsonProperty("description") String description){
		this.errors = errors;
		this.status = status;
		this.timestamp = timestamp;
		this.resultado = resultado;
		this.id_caso = id_caso;
		this.description = description;
	}
}
