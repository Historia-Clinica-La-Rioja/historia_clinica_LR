package ar.lamansys.sgx.shared.featureflags.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

public interface FeatureFlagsService {

    boolean isOn(AppFeature feature);
}
