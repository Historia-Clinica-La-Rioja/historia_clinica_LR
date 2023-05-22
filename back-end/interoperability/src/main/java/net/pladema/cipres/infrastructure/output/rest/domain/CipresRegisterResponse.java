package net.pladema.cipres.infrastructure.output.rest.domain;

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
@JsonDeserialize(using = CipresRegisterResponseDeserializer.class)
public class CipresRegisterResponse {

	@ToString.Include
	private String detail;

	@ToString.Include
	private String type;

	@ToString.Include
	private String title;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public CipresRegisterResponse(@JsonProperty("detail") String detail,
								  @JsonProperty("type") String type,
								  @JsonProperty("title") String title) {
		this.detail = detail;
		this.title = title;
		this.type = type;
	}

}
