package net.pladema.sipplus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class SipPlusPregnancieResponse {
	private Map<String, Object> pregnancies;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public SipPlusPregnancieResponse(@JsonProperty("pregnancies") Map<String, Object> pregnancies ) {
		this.pregnancies = pregnancies;
	}
}
