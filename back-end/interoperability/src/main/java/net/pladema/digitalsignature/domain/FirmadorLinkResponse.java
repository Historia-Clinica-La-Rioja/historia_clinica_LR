package net.pladema.digitalsignature.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class FirmadorLinkResponse {

	private String link;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public FirmadorLinkResponse(@JsonProperty("Location") String link) {
		this.link = link;
	}
}
