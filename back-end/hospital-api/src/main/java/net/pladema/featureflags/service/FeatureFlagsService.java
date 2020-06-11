package net.pladema.featureflags.service;

import net.pladema.sgx.featureflags.AppFeature;

public interface FeatureFlagsService {

    boolean isOn(AppFeature feature);
}
