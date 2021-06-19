package net.pladema.featureflags.service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

public interface FeatureFlagsService {

    boolean isOn(AppFeature feature);
}
