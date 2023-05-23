package net.pladema.terminology.cache.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

public class TerminologyCSVDto {
	public final SnomedECL ecl;
	public final String url;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public TerminologyCSVDto(
			@JsonProperty("ecl") SnomedECL ecl,
			@JsonProperty("url") String url
	) {
		this.ecl = ecl;
		this.url = url;
	}
}
