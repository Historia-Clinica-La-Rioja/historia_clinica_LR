package ar.lamansys.sgx.shared.publicinfo.infrastructure.input.dto;

import java.util.Set;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PublicInfoDto {
	public final Set<AppFeature> features;
}
