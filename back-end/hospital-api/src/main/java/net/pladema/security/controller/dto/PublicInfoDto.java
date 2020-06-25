package net.pladema.security.controller.dto;

import java.util.Set;

import net.pladema.sgx.featureflags.AppFeature;

public class PublicInfoDto {
	public final String flavor;
	public final Set<AppFeature> features;

	public PublicInfoDto(String flavor, Set<AppFeature> features) {
		this.flavor = flavor;
		this.features = features;
	}
}

