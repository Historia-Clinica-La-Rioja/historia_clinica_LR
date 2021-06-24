package ar.lamansys.sgx.shared.publicinfo.infrastructure.input.dto;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

import java.util.Set;

public class PublicInfoDto {
	public final String flavor;
	public final Set<AppFeature> features;

	public PublicInfoDto(String flavor, Set<AppFeature> features) {
		this.flavor = flavor;
		this.features = features;
	}
}

