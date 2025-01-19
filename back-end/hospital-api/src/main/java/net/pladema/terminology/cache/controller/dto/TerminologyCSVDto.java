package net.pladema.terminology.cache.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

public class TerminologyCSVDto {
	public final SnomedECL ecl;
	public final String url;
	public final ETerminologyKind kind;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public TerminologyCSVDto(
			@JsonProperty("ecl") SnomedECL ecl,
			@JsonProperty("url") String url,
			@JsonProperty("kind") ETerminologyKind kind
	) {
		this.ecl = ecl;
		this.url = url;
		this.kind = kind;
	}
}
